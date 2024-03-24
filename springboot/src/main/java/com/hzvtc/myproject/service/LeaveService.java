package com.hzvtc.myproject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.dao.LeaveMapper;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.Leave;
import com.hzvtc.myproject.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-03-10
 */
@Service
public class LeaveService {
    @Autowired
    private LeaveMapper leaveMapper;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private StudentService studentService;

    public void save(Leave leave) {
        leaveMapper.save(leave);
        studentService.updateLeave(new Student().setIsLeave(true).setId(leave.getStudentId()));
    }

    public void update(Long id) {
        leaveMapper.update(id);
        Leave leave = query(id);
        studentService.updateLeave(new Student().setIsLeave(false).setId(leave.getStudentId()));
    }

    public PageInfo<Leave> list(ListQuery<Leave> listQuery, Long bid) {
        Leave leave = listQuery.getEntity();
        List<Long> bIds = buildingService.getIdsByParentId(bid);
        PageHelper.startPage(listQuery.getPage(), listQuery.getRows());
        List<Leave> list = leaveMapper.list(leave, bIds);
        return new PageInfo<>(list);
    }

    public Leave query(Long id) {
        return leaveMapper.query(id);
    }

    public void delete(Long id) {
        Leave leave = query(id);
        if (leave != null) {
            leaveMapper.delete(id);
            studentService.updateLeave(new Student().setIsLeave(false).setId(leave.getStudentId()));
        }
    }
}
