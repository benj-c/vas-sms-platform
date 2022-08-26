package com.sys.vas.datamodel.repository;

import com.sys.vas.datamodel.entity.SysConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SysConfigRepository extends JpaRepository<SysConfigEntity, Long> {

    @Modifying
    @Query("update SysConfigEntity sys set sys.smsInCount = sys.smsInCount + 1")
    void incrementInSmsCount();

    @Modifying
    @Query("update SysConfigEntity sys set sys.smsOutCount = sys.smsOutCount + 1")
    void incrementOutSmsCount();
}
