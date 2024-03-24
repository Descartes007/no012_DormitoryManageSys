package com.hzvtc.myproject.service;

import com.hzvtc.myproject.dao.FacultyMapper;
import com.hzvtc.myproject.entity.Faculty;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.utils.HttpCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2021-01-19
 */
@Service
public class FacultyService {
    @Autowired
    private FacultyMapper facultyMapper;
    @Autowired
    private StudentService studentService;

    public List<Faculty> list() {
        List<Faculty> res = facultyMapper.list(null);
        setStudentNum(res);
        return res;
    }

    private void setStudentNum(List<Faculty> list) {
        for (Faculty faculty: list) {
            faculty.setStudentNum(studentService.countByFacultyId(faculty.getId()));
            setStudentNum(faculty.getChildren());
        }
    }

    public void insert(Faculty faculty) {
        facultyMapper.insert(faculty);
    }

    public void delete(Long id) {
        int count = studentService.countByFacultyId(id);
        if (count > 0) {
            throw new HttpException(HttpCode.FAILED, "该节点或子节点下还有学生，无法删除");
        }
        List<Long> list = facultyMapper.listByParentId(id);
        list.forEach(this::delete);
        facultyMapper.delete(id);
    }

    public Optional<Faculty> query(Long id) {
        return facultyMapper.query(id);
    }

    public void update(Faculty faculty) {
        facultyMapper.update(faculty);
    }

    public List<Long> getIdsByParentId(Long fId) {
        List<Long> res = new ArrayList<>(16);
        res.add(fId);
        getIdsByParentId(fId, res);
        return res;
    }

    public List<Faculty> listAll() {
        return facultyMapper.listAll();
    }

    private void getIdsByParentId(Long fId, List<Long> result) {
        List<Long> childrenId = facultyMapper.listByParentId(fId);
        result.addAll(childrenId);
        childrenId.forEach(l -> getIdsByParentId(l, result));
    }
}
