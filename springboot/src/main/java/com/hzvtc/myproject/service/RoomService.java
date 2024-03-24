package com.hzvtc.myproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.dao.RoomMapper;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2021-01-22
 */
@Service
public class RoomService {
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private RepairService repairService;

    public List<Room> listByBuildingId(Long bid) {
        List<Long> bids = buildingService.getIdsByParentId(bid);
        return roomMapper.listByBuildingId(bids);
    }

    public PageInfo<Room> list(ListQuery<Room> listQuery, Long bid) {
        List<Long> bList = buildingService.getIdsByParentId(bid);
        Room room = listQuery.getEntity();
        List<Long> selectBid = null;
        if (room.getBuildingId() != null) {
            selectBid = buildingService.getIdsByParentId(room.getBuildingId());
        }
        PageHelper.startPage(listQuery.getPage(), listQuery.getRows());
        List<Room> rooms = roomMapper.list(room, bList, selectBid);
        return new PageInfo<>(rooms);
    }

    public List<Room> list(Long bid) {
        List<Long> bList = buildingService.getIdsByParentId(bid);
        return roomMapper.list(new Room(), bList, null);
    }

    public void saveOrUpdate(Room room) {
        if (room.getId() == null) {
            roomMapper.save(room);
        } else {
            roomMapper.update(room);
        }
    }

    public void delete(Long id) {
        repairService.deleteByRoomId(id);
        roomMapper.delete(id);
    }

    public Optional<Room> query(Long id) {
        return roomMapper.query(id);
    }

    public int getMaxCapacity(Long id) {
        return roomMapper.getMaxCapacity(id);
    }

    public int count(Long bid) {
        List<Long> bids = buildingService.getIdsByParentId(bid);
        return roomMapper.count(bids);
    }
}
