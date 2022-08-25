package com.sys.vas.management.repository;

import com.sys.vas.management.dto.ApiResponseDto;
import com.sys.vas.management.dto.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiRepository extends JpaRepository<ApiEntity, Long> {

    @Query("select new com.sys.vas.management.dto.ApiResponseDto(api.id, api.description, api.name, his.version) from ApiEntity api, ApiHistoryEntity his where api.id = his.api.id and his.isActive = true")
    List<ApiResponseDto> getActiveApis();
}
