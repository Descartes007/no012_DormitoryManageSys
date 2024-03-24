package com.hzvtc.myproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.dao.RepairMapper;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.Repair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiongXinxin
 * @date 2021-03-13
 */
@Service
public class RepairService {
    @Autowired
    private RepairMapper repairMapper;
    @Autowired
    private BuildingService buildingService;

    public void save(Repair repair) {
        repairMapper.save(repair);
        repairMapper.deletePicture(repair.getId());
        savePicture(repair.getId(), repair.getPicture());
    }

    public void update(Repair repair) {
        repairMapper.update(repair);
        repairMapper.deletePicture(repair.getId());
        savePicture(repair.getId(), repair.getPicture());
    }

    private void savePicture(Long repairId, List<String> pictures) {
        if (pictures != null) {
            for (String pic: pictures) {
                repairMapper.savePicture(repairId, pic);
            }
        }
    }

    public void updateStatus(Long id){
        repairMapper.updateStatus(id);
    }

    public PageInfo<Repair> list(ListQuery<Repair> listQuery, Long bid) {
        List<Long> bids = buildingService.getIdsByParentId(bid);
        PageHelper.startPage(listQuery.getPage(), listQuery.getRows());
        List<Repair> list = repairMapper.list(listQuery.getEntity(), bids);
        return PageInfo.of(list);
    }

    public Repair query(Long id) {
        return repairMapper.query(id);
    }

    public void delete(Long id) {
        repairMapper.deletePicture(id);
        repairMapper.delete(id);
    }

    public void deleteByRoomId(Long roomId) {
        repairMapper.deletePictureByRoomId(roomId);
        repairMapper.deleteByRoomId(roomId);
    }
}
