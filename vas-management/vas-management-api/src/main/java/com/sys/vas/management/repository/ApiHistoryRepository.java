package com.sys.vas.management.repository;

import com.sys.vas.management.dto.ApiHistoryVersionDto;
import com.sys.vas.management.dto.entity.ApiHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApiHistoryRepository extends JpaRepository<ApiHistoryEntity, Long> {

    @Query("select new com.sys.vas.management.dto.ApiHistoryVersionDto(his.id, his.api.id, his.commitId, his.commitMessage, his.version, his.isActive) from ApiHistoryEntity his where his.api.id = :id")
    List<ApiHistoryVersionDto> findAllByApi(@Param("id") long id);

    @Query("from ApiHistoryEntity api where api.api.id = :id")
    List<ApiHistoryEntity> findAllByApiId(@Param("id") long id);

    @Modifying
    @Query("update ApiHistoryEntity his set his.isActive = case when (his.commitId = :commitId) then true else false end where his.api.id = :apiId")
    void updateApiSetActive(@Param("apiId") long apiId, @Param("commitId") String commitId);
}
