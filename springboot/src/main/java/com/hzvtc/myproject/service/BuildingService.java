package com.hzvtc.myproject.service;

import com.hzvtc.myproject.dao.BuildingMapper;
import com.hzvtc.myproject.entity.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2021-01-19
 */
@Service
public class BuildingService {
    @Autowired
    private BuildingMapper buildingMapper;

    public List<Building> list() {
        List<Building> list = buildingMapper.list(null);
        traverseTree(list);
        return list;
    }

    public Optional<Building> query(Long id) {
        return buildingMapper.query(id);
    }

    public List<Building> listAll() {
        return buildingMapper.listAll();
    }

    private void traverseTree (List<Building> list) {
        list.forEach(building -> {
            List<Long> result = new ArrayList<>(16);
            result.add(building.getId());
            getIdsByParentId(building.getId(), result);
            int studentNum = buildingMapper.countStudentByBuildingIds(result);
            int roomNum = buildingMapper.countRoomByBuildingIds(result);
            building.setStudentNum(studentNum);
            building.setRoomNum(roomNum);
            traverseTree(building.getChildren());
        });
    }

    public List<Long> getIdsByParentId(Long bId) {
        List<Long> res = new ArrayList<>(16);
        res.add(bId);
        getIdsByParentId(bId, res);
        return res;
    }

    private void getIdsByParentId(Long bId, List<Long> result) {
        List<Long> childrenIds = buildingMapper.listByParentId(bId);
        result.addAll(childrenIds);
        childrenIds.forEach(l -> getIdsByParentId(l,result));
    }

    public void save(Building building) {
        buildingMapper.insert(building);
    }

    public void update(Building building) {
        buildingMapper.update(building);
    }

    public void delete(Long id) {
        List<Long> list = buildingMapper.listByParentId(id);
        list.forEach(this::delete);
        buildingMapper.delete(id);
    }
}
