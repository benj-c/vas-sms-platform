package com.sys.vas.management.repository;

import com.sys.vas.management.dto.ApiResponseDto;
import com.sys.vas.management.dto.ApiXmlResponseDto;
import com.sys.vas.management.dto.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApiRepository extends JpaRepository<ApiEntity, Long> {

    @Query("select new com.sys.vas.management.dto.ApiResponseDto(api.id, api.description, api.name, his.version, his.commitId) from ApiEntity api, ApiHistoryEntity his where api.id = his.api.id and his.isActive = true")
    List<ApiResponseDto> getActiveApis();

    @Query("select new com.sys.vas.management.dto.ApiXmlResponseDto(api.id, api.name, api.description, his.version, his.commitId, his.commitMessage, his.isActive, his.xml, his.totalRequestsCount, his.avgResTime, his.errorCount) from ApiEntity api, ApiHistoryEntity his where api.id = his.api.id and api.id = :id and his.commitId = :commit")
    Optional<ApiXmlResponseDto> findApiByIdAndVersion(@Param("id") long id, @Param("commit") String commit);
}
