package com.hzvtc.myproject.scheduled;

import com.hzvtc.myproject.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时删除日志
 *
 * @author 熊新欣
 * @date 2021-01-21
 */
@Component
public class LogClean {
    @Autowired
    private SystemLogService systemLogService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void logClean() {
        systemLogService.deleteByMonth();
    }
}
