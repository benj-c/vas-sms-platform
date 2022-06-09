package com.sys.vas.datamodel.repository;

import com.sys.vas.datamodel.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepository extends JpaRepository<ApiEntity, Long> {
}
