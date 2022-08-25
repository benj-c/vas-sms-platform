package com.sys.vas.management.repository;

import com.sys.vas.management.dto.entity.ApiHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiHistoryRepository extends JpaRepository<ApiHistoryEntity, Long> {
}
