package com.sys.vas.management.repository;

import com.sys.vas.management.dto.entity.SysActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysActionLogRepository extends JpaRepository<SysActionLog, Long> {
}
