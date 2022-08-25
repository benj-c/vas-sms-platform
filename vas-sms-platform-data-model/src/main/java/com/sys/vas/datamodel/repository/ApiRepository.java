package com.sys.vas.datamodel.repository;

import com.sys.vas.datamodel.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApiRepository extends JpaRepository<ApiEntity, Long> {

    Optional<ApiEntity> findByName(String name);

}
