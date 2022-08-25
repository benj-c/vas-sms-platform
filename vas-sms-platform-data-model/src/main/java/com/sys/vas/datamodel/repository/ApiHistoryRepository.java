package com.sys.vas.datamodel.repository;

import com.sys.vas.datamodel.entity.ApiEntity;
import com.sys.vas.datamodel.entity.ApiHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApiHistoryRepository extends JpaRepository<ApiHistoryEntity, Long> {

    @Query("select his from ApiEntity api, ApiHistoryEntity his where api.id = his.api.id and api.name = :name and his.isActive = true")
    Optional<ApiHistoryEntity> findActiveApiByName(@Param("name") String name);

}
