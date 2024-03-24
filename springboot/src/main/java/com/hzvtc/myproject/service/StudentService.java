package com.hzvtc.myproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.dao.StudentMapper;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.Student;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.utils.HttpCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2021-01-22
 */
@Service
public class StudentService {
    private final StudentMapper studentMapper;
    private final BuildingService buildingService;
    @Autowired
    private FacultyService facultyService;
    private final RoomService roomService;

    public StudentService(StudentMapper studentMapper, BuildingService buildingService, RoomService roomService) {
        this.studentMapper = studentMapper;
        this.buildingService = buildingService;
        this.roomService = roomService;
    }

    public void saveOrUpdate(Student student) {
        int maxCapacity = roomService.getMaxCapacity(student.getRoomId());
        int count = studentMapper.countByRoomId(student.getRoomId());
        if (student.getId() == null) {
            if (count >= maxCapacity) {
                throw new HttpException(HttpCode.FAILED, "所选寝室已满");
            }
            studentMapper.save(student);
        } else {
            Student oldData = studentMapper.query(student.getId()).get();
            if (!oldData.getRoomId().equals(student.getRoomId())) {
                if (count >= maxCapacity) {
                    throw new HttpException(HttpCode.FAILED, "所选寝室已满");
                }
            }
            studentMapper.update(student);
        }
    }

    public void delete(Long id) {
        studentMapper.delete(id);
    }

    public Optional<Student> query(Long id) {
        return studentMapper.query(id);
    }

    public List<Student> list(String name, Long buildingId) {
        List<Long> buildingIds = buildingService.getIdsByParentId(buildingId);
        return studentMapper.list(new Student().setName(name), null, buildingIds);
    }

    public PageInfo<Student> list(ListQuery<Student> listQuery, Long buildingId) {
        List<Long> facultyIds = null;
        Student student = listQuery.getEntity();
        if (student.getFacultyId() != null) {
            facultyIds = facultyService.getIdsByParentId(student.getFacultyId());
        }
        List<Long> buildingIds = buildingService.getIdsByParentId(buildingId);
        PageHelper.startPage(listQuery.getPage(), listQuery.getRows());
        List<Student> studentList = studentMapper.list(student, facultyIds, buildingIds);
        return new PageInfo<>(studentList);
    }

    public int countByRoomId(Long rid) {
        return studentMapper.countByRoomId(rid);
    }

    public int countByFacultyId(Long fid) {
        List<Long> fids = facultyService.getIdsByParentId(fid);
        return studentMapper.countByFacultyId(fids);
    }

    public void updateLeave(Student student) {
        studentMapper.updateLeave(student);
    }
}
