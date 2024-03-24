package com.hzvtc.myproject.service;

import com.hzvtc.myproject.dao.SystemFunctionMapper;
import com.hzvtc.myproject.entity.SystemFunction;
import com.hzvtc.myproject.entity.SystemRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author 熊新欣
 * @date 2020-12-09
 */
@Service
public class SystemFunctionService {
    @Autowired
    private SystemFunctionMapper systemFunctionMapper;

    public List<SystemFunction> listFunctionByParentIdAndIds(Long parentId, List<Long> roleList) {
        if (roleList.size() == 0) {
            return new ArrayList<>(0);
        }
        List<SystemFunction> res = systemFunctionMapper.listFunctionByParentIdAndIds(parentId, roleList);
        res.forEach(systemFunction -> {
            List<SystemFunction> children = listFunctionByParentIdAndIds(systemFunction.getId(), roleList);
            systemFunction.setChildren(children);
        });
        return res;
    }

    public Set<String> getPermission(List<SystemRole> roleList) {
        return systemFunctionMapper.getPermission(roleList);
    }

    public List<SystemFunction> listTree() {
        return systemFunctionMapper.getTree(null);
    }

    public List<Long> selectMenuListByRoleId(Long roleId) {
        return systemFunctionMapper.selectMenuListByRoleId(roleId);
    }

    public void delete(Long id) {
        List<SystemFunction> list = systemFunctionMapper.listByParentId(id);
        list.forEach(function -> delete(function.getId()));
        systemFunctionMapper.deleteRoleFunctionByFunctionId(id);
        systemFunctionMapper.delete(id);
    }

    public void saveOrUpdate(SystemFunction function) {
        if (function.getId() == null) {
            systemFunctionMapper.save(function);
        } else {
            systemFunctionMapper.update(function);
        }
    }

    public void save(SystemFunction function) {
        systemFunctionMapper.save(function);
    }

    public void update(SystemFunction function) {
        systemFunctionMapper.update(function);
    }

    public Optional<SystemFunction> query(Long id) {
        return systemFunctionMapper.query(id);
    }

}
