package com.sys.vas.management.repository;

import com.sys.vas.management.dto.entity.SysActionLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysActionLogRepository extends JpaRepository<SysActionLog, Long> {

    List<SysActionLog> findAllByOrderByTimestampDesc(Pageable pageable);
}
