package com.sys.vas.management.repository;

import com.sys.vas.management.dto.entity.SmsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmsHistoryRepository extends JpaRepository<SmsHistoryEntity, Long> {

    List<SmsHistoryEntity> findByMsisdnOrderByIdDesc(String msisdn);
}
