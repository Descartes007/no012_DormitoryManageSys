package com.hzvtc.myproject.dao;

import com.hzvtc.myproject.entity.Room;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2021-01-22
 */
@Repository
public interface RoomMapper {

    /**
     * 插入
     * @param room 。
     */
    void save(Room room);

    /**
     * 列表
     * @param buildingId 。
     * @return 。
     */
    List<Room> listByBuildingId(List<Long> buildingId);
    /**
     * 查询列表
     * @param room 查询条件
     * @param bid 用户所管理的楼 及 子楼id
     * @param selectBid 需要查询的建筑id 及 子id
     * @return 结果
     */
    List<Room> list(Room room, @Param("bid") List<Long> bid, List<Long> selectBid);

    /**
     * 详情
     * @param id
     * @return
     */
    Optional<Room> query(Long id);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 更新
     * @param room
     */
    void update(Room room);

    /**
     * 获取最大容量
     * @param id
     * @return
     */
    int getMaxCapacity(Long id);

    /**
     * 寝室数量
     * @param bid
     * @return
     */
    int count(List<Long> bid);
}
