package com.sys.vas.management.service;

import com.sys.vas.management.dto.entity.SysActionLog;
import com.sys.vas.management.repository.SysActionLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SysActionLogService {

    private SysActionLogRepository sysActionLogRepository;

    public SysActionLogService(SysActionLogRepository sysActionLogRepository) {
        this.sysActionLogRepository = sysActionLogRepository;
    }

    void logEvent(String txt) {
        SysActionLog slog = new SysActionLog();
        slog.setLog(txt);
        slog.setTimestamp(LocalDateTime.now());

        sysActionLogRepository.saveAndFlush(slog);
    }
}
