package com.sys.vas.datamodel.repository;

import com.sys.vas.datamodel.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiRepository extends JpaRepository<ApiEntity, Long> {

    Optional<ApiEntity> findByName(String name);
}
