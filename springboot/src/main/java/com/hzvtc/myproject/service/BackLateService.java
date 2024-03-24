package com.hzvtc.myproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.dao.BackLateMapper;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.BackLate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-02-16
 */
@Service
public class BackLateService {
    @Autowired
    private BackLateMapper backLateMapper;

    @Autowired
    private BuildingService buildingService;

    public PageInfo<BackLate> list(ListQuery<BackLate> listQuery, Long bid) {
        List<Long> bIds = buildingService.getIdsByParentId(bid);
        BackLate backLate = listQuery.getEntity();
        PageHelper.startPage(listQuery.getPage(), listQuery.getRows());
        List<BackLate> backLateList = backLateMapper.list(backLate, bIds);
        return new PageInfo<>(backLateList);
    }

    public void saveOrUpdate(BackLate backLate) {
        if (backLate.getId() == null) {
            backLateMapper.save(backLate);
        } else {
            backLateMapper.update(backLate);
        }
    }

    public void delete(Long id) {
        backLateMapper.delete(id);
    }

    public BackLate query(Long id) {
        return backLateMapper.query(id);
    }
}
