package com.sys.vas.management.repository;

import com.sys.vas.management.dto.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRepository extends JpaRepository<ApiEntity, Long> {

}
