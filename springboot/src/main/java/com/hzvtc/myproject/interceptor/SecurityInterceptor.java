package com.hzvtc.myproject.interceptor;

import com.hzvtc.myproject.annotation.RequirePermission;
import com.hzvtc.myproject.entity.SystemRole;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.service.SystemFunctionService;
import com.hzvtc.myproject.service.SystemRoleService;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.utils.HttpCode;
import com.hzvtc.myproject.utils.Match;
import com.hzvtc.myproject.utils.RedisUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 权限拦截器
 * @author 熊新欣
 * @date 2020-12-08
 */
@Component
public class SecurityInterceptor implements HandlerInterceptor {
    private final RedisUtil redisUtil;
    private final SystemFunctionService systemFunctionService;
    private final SystemRoleService systemRoleService;
    private static final Map<Match, Validate> VALIDATE_MAP = new HashMap<>();

    static {
        VALIDATE_MAP.put(Match.HAS_ANY, (userPermission, methodPermission) -> {
            for (String up : userPermission) {
                for (String mp : methodPermission) {
                    if (up.equalsIgnoreCase(mp)) {
                        return true;
                    }
                }
            }
            return false;
        });
        VALIDATE_MAP.put(Match.HAS_ALL, (userPermission, methodPermission) -> {
            int vote = 0;
            for (String up : userPermission) {
                for (String mp : methodPermission) {
                    if (up.equalsIgnoreCase(mp)) {
                        vote++;
                    }
                }
            }
            return vote == methodPermission.length;
        });
    }

    public SecurityInterceptor(RedisUtil redisUtil, SystemFunctionService systemFunctionService, SystemRoleService systemRoleService) {
        this.redisUtil = redisUtil;
        this.systemFunctionService = systemFunctionService;
        this.systemRoleService = systemRoleService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //获取请求的方法
        HandlerMethod handlerMethod;
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
        } else {
            //404
            return true;
        }
        Method method = handlerMethod.getMethod();
        //获取请求方法所需的权限
        String[] requiredPermissions;
        Match match;
        if (method.isAnnotationPresent(RequirePermission.class)) {
            RequirePermission hasPermission = method.getAnnotation(RequirePermission.class);
            requiredPermissions = hasPermission.permissions();
            match = hasPermission.matchType();
        } else {
            //方法不需要权限（无 RequirePermission 注解）
            return true;
        }

        String token = request.getHeader(Constant.HEADER_TOKEN);

        Long id = redisUtil.get(token);

        //获取该用户的权限
        List<SystemRole> roleList = systemRoleService.listByUserId(id);
        Set<String> permissions;
        if (roleList.size() == 0) {
            permissions = new HashSet<>();
        } else {
            permissions = systemFunctionService.getPermission(roleList);
        }

        //验证权限
        if (VALIDATE_MAP.get(match).validate(permissions, requiredPermissions)) {
            return true;
        }
        //权限验证失败
        throw new HttpException(HttpCode.HAS_NO_PERMISSIONS, "没有权限，请联系管理员");
    }


    private interface Validate {
        /**
         * 验证权限
         * @param userPermission 用户拥有的权限
         * @param methodPermission 方法需要的权限
         * @return 是否通过
         */
        Boolean validate(Set<String> userPermission, String[] methodPermission);
    }
}
