package com.hzvtc.myproject.dao;

import com.hzvtc.myproject.entity.Student;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2021-01-21
 */
@Repository
public interface StudentMapper {

    /**
     * 插入
     * @param student
     */
    void save(Student student);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 查询单个
     * @param id
     * @return
     */
    Optional<Student> query(Long id);

    /**
     * 更新
     * @param student
     */
    void update(Student student);

    /**
     * 查询列表
     * @param student 查询条件
     * @param facultyIds 查询条件中 的 学院 以及 子学院
     * @param buildingIds 用户所管理的楼 以及及 子楼
     * @return
     */
    List<Student> list(@Param("s") Student student,@Param("fid") List<Long> facultyIds, @Param("bid") List<Long> buildingIds);

    /**
     * 根据room id 获取学生列表
     * @param roomId
     * @return
     */
    int countByRoomId(Long roomId);

    /**
     * 根据faculty id 获取学生列表
     * @param facultyId
     * @return
     */
    int countByFacultyId(List<Long> facultyId);

    /**
     * 更换照片
     * @param student
     */
    void changePhoto(Student student);

    /**
     * 更新在寝状态
     * @param student
     */
    void updateLeave(Student student);
}
