package com.sys.vas.datamodel.repository;

import com.sys.vas.datamodel.entity.ApiHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApiHistoryRepository extends JpaRepository<ApiHistoryEntity, Long> {

    @Query("select his from ApiEntity api, ApiHistoryEntity his where api.id = his.api.id and api.name = :name and his.isActive = true")
    Optional<ApiHistoryEntity> findActiveApiByName(@Param("name") String name);

    @Modifying
    @Query("update ApiHistoryEntity his set his.totalRequestsCount = his.totalRequestsCount + 1, his.avgResTime = :avgResTime + his.avgResTime, his.errorCount = :errorCount + his.errorCount where his.api.id = (select api.id from ApiEntity api where api.name =:apiName) and his.isActive = true")
    void updateStats(
            @Param("apiName") String apiName,
            @Param("avgResTime") Long avgResTime,
            @Param("errorCount") Long errorCount
    );
}
