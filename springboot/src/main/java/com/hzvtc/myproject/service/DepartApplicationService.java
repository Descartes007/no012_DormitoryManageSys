package com.hzvtc.myproject.service;

import com.hzvtc.myproject.dao.DepartApplicationMapper;
import com.hzvtc.myproject.entity.DepartApplication;
import com.hzvtc.myproject.entity.DepartApplicationUser;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.utils.HttpCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-02-09
 */
@Service
public class DepartApplicationService {

    @Autowired
    private DepartApplicationMapper departApplicationMapper;

    public void saveApplication(DepartApplication departApplication) {
        departApplicationMapper.saveApplication(departApplication);
    }

    public void saveApplication(Long userId, Long applicationId) {
        departApplicationMapper.saveApplicationUser(userId, applicationId);
    }

    public List<DepartApplication> listMyApplication(Long userId) {
        List<DepartApplication> list = departApplicationMapper.listMyApplication(userId);
        list.forEach(departApplication -> {
            long id = departApplication.getId();
            departApplication.setIsFinished(departApplicationMapper.countFlowAgreeIsNotNull(id) == 0);
            DepartApplicationUser applicationUser = departApplicationMapper.getLastFlow(id);
            if (applicationUser == null) {
                departApplication.setIsPass(true);
            } else {
                departApplication.setIsPass(applicationUser.getIsAgree());
            }
        });
        return list;
    }

    public List<DepartApplicationUser> listMyFlow(Long userId) {
        return departApplicationMapper.listMyFlow(userId);
    }

    public List<DepartApplicationUser> listApplicationFlow(Long id) {
        return departApplicationMapper.listApplicationFlow(id);
    }

    public DepartApplication getApplication(Long id) {
        return departApplicationMapper.getApplication(id);
    }

    public DepartApplicationUser getApplicationUser(Long id) {
        return departApplicationMapper.getApplicationUser(id);
    }

    public void update(DepartApplicationUser departApplicationUser) {
        departApplicationMapper.update(departApplicationUser);
    }

    public void deleteApplication(Long id) {
        if (departApplicationMapper.countFlowAgreeIsNotNull(id) == 0){
            throw new HttpException(HttpCode.FAILED, "审核已完成，无法撤销");
        } else if (departApplicationMapper.countFlow(id) > 0) {
            throw new HttpException(HttpCode.FAILED, "申请已被审核，无法撤销");
        }  else {
            departApplicationMapper.deleteFlow(id);
            departApplicationMapper.deleteApplication(id);
        }
    }

    public int countFlowAgreeIsNull(Long userId) {
        return departApplicationMapper.countFlowAgreeIsNull(userId);
    }

    public DepartApplication getByDepartApplicationUserId(Long applicationUserId) {
        return departApplicationMapper.getByDepartApplicationUserId(applicationUserId);
    }
}
