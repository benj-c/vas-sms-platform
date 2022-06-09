package com.sys.vas.management.repository;

import com.sys.vas.management.dto.CxResponseDto;
import com.sys.vas.management.dto.entity.CxResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CxResponseRepository extends JpaRepository<CxResponseEntity, Long> {

    @Query("select new com.sys.vas.management.dto.CxResponseDto(cx.id, cx.resCode, cx.resDesc, cx.sendToOriginatedNo, cx.sms, cx.api.id) from CxResponseEntity cx where cx.api.id = :apiId")
    List<CxResponseDto> findByApiId(long apiId);
}
