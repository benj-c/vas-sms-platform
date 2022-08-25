package com.sys.vas.management.repository;

import com.sys.vas.management.dto.ApiHistoryVersionDto;
import com.sys.vas.management.dto.entity.ApiHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApiHistoryRepository extends JpaRepository<ApiHistoryEntity, Long> {

    @Query("select new com.sys.vas.management.dto.ApiHistoryVersionDto(his.id, his.api.id, his.commitId, his.commitMessage, his.version, his.isActive) from ApiHistoryEntity his where his.api.id = :id")
    List<ApiHistoryVersionDto> findAllByApiId(@Param("id") long id);
}
