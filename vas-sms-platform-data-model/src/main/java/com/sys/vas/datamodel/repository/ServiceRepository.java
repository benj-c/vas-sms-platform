package com.sys.vas.datamodel.repository;

import com.sys.vas.datamodel.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
}
