package com.hzvtc.myproject.service;

import com.hzvtc.myproject.dao.SystemUserMapper;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.socket.WebSocket;
import com.hzvtc.myproject.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2020-12-09
 */
@Service
public class SystemUserService {
    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private SystemRoleService systemRoleService;

    public List<SystemUser> listUserByLoginName(String loginName) {
        return systemUserMapper.listUserByLoginName(loginName);
    }

    public Optional<SystemUser> info(Long id) {
        return systemUserMapper.getInfo(id);
    }

    public List<SystemUser> listByLeaderId(Long id) {
        return systemUserMapper.listByLeaderId(id);
    }

    public void saveOrUpdate(SystemUser user) {
        List<Long> roleList = user.getUserRoleId();
        if (user.getId() == null) {
            user.setPassword(MD5Util.md5(Constant.DEFAULT_PASSWORD));
            systemUserMapper.save(user);
        } else {
            user.setPassword(MD5Util.md5(user.getPassword()));
            systemUserMapper.update(user);
        }
        if (roleList != null) {
            systemUserMapper.deleteUserRoleByUserId(user.getId());
            roleList.forEach(role -> systemRoleService.insertUserRole(user.getId(), role));
        }
    }

    public Boolean validatePassword(String password, Long id) {
        String old = systemUserMapper.validatePassword(id);
        return MD5Util.md5(password).equals(old);
    }

    public Boolean validateLoginName(String loginName, Long userId) {
        List<SystemUser> list = systemUserMapper.validateLoginName(loginName, userId);
        return list.size() == 0;
    }

    public void changePassword(String password, Long id) {
        SystemUser user = new SystemUser();
        user.setId(id);
        user.setPassword(MD5Util.md5(password));
        systemUserMapper.changePassword(user);
    }

    public void delete(Long id) {
        systemUserMapper.deleteUserRoleByUserId(id);
        systemUserMapper.delete(id);
    }

    public Optional<SystemUser> get(Long id) {
        Optional<SystemUser> systemUser = systemUserMapper.get(id);
        systemUser.ifPresent(user -> user.setIsOnLine(WebSocket.WEB_SOCKET_SET.containsKey(user.getLoginName())));
        return systemUser;
    }


    public List<SystemUser> listByIds(List<Long> ids) {
        List<SystemUser> list = systemUserMapper.listByIds(ids);
        list.forEach(systemUser -> {
                    systemUser.setIsOnLine(WebSocket.WEB_SOCKET_SET.containsKey(systemUser.getLoginName()));
                    systemUser.setUserRole(systemRoleService.listByUserId(systemUser.getId()));
                });
        return list;
    }

    public List<Long> listIdFitCondition(SystemUser user) {
        return systemUserMapper.listIdFitCondition(user);
    }

    public void changeIcon(String fileName, Long id) {
        systemUserMapper.changeIcon(fileName, id);
    }

    public List<SystemUser> listAll() {
        return systemUserMapper.listAll();
    }

    public List<Long> listByBuildingIds(List<Long> buildingIds) {
        return systemUserMapper.listByBuildingIds(buildingIds);
    }
}
