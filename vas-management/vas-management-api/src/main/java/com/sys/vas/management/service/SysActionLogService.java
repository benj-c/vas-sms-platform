package com.sys.vas.management.service;

import com.sys.vas.management.dto.UserAction;
import com.sys.vas.management.dto.entity.SysActionLog;
import com.sys.vas.management.repository.SysActionLogRepository;
import com.sys.vas.management.util.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysActionLogService {

    @Autowired
    private SysActionLogRepository sysActionLogRepository;

    public SysActionLogService() {
    }

    /**
     * @param userAction
     */
    void logEvent(UserAction userAction) {
        SysActionLog slog = new SysActionLog();
        slog.setLog(new StringBuilder()
                .append(userAction.getType()).append("|")
                .append(userAction.getTarget()).append("|")
                .append(userAction.getComment()).append("|")
                .append(ApiUtil.getAuthUserName())
                .toString()
        );
        slog.setTimestamp(LocalDateTime.now());

        sysActionLogRepository.saveAndFlush(slog);
    }

    /**
     * @return
     */
    public List<UserAction> getUserActions() {
        return sysActionLogRepository.findAllByOrderByTimestampDesc(PageRequest.of(0, 10))
                .stream()
                .map(m -> {
                    String[] arr = m.getLog().split("\\|");
                    System.out.println(arr[0]);
                    return UserAction.builder()
                            .timestamp(m.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                            .type(arr[0])
                            .target(arr[1])
                            .comment(arr[2])
                            .user(arr[3])
                            .build();
                }).collect(Collectors.toList());
    }
}
