package com.hzvtc.myproject.service;

import com.hzvtc.myproject.dao.SystemRoleMapper;
import com.hzvtc.myproject.entity.SystemRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2020-12-09
 */
@Service
public class SystemRoleService {
    @Autowired
    private SystemRoleMapper systemRoleMapper;

    @Autowired
    private SystemFunctionService systemFunctionService;

    public List<SystemRole> listByUserId(Long id) {
        return systemRoleMapper.listByUserId(id);
    }

    public List<SystemRole> listAll(SystemRole role) {
        return systemRoleMapper.listAll(role);
    }


    public void deleteRoleByRoleId(Long roleId) {
        systemRoleMapper.deleteUserRoleByRoleId(roleId);
        systemRoleMapper.delete(roleId);
    }

    public void insertUserRole(Long userId, Long roleId) {
        systemRoleMapper.insertUserRole(userId, roleId);
    }

    public void delete(Long id) {
        systemRoleMapper.deleteRoleFunctionByRoleId(id);
        systemRoleMapper.deleteUserRoleByRoleId(id);
        systemRoleMapper.delete(id);
    }

    public void saveOrUpdate(SystemRole role) {
        if (role.getId() != null) {
            systemRoleMapper.update(role);
        } else {
            systemRoleMapper.save(role);
        }
        systemRoleMapper.deleteRoleFunctionByRoleId(role.getId());
        if (role.getFunctionIds() != null) {
            List<Long> functionIdList = role.getFunctionIds();
            functionIdList.forEach(l -> systemRoleMapper.insertRoleFunction(role.getId(), l));
        }
    }

    public Optional<SystemRole> query(Long id) {
        return systemRoleMapper.query(id);
    }
}
