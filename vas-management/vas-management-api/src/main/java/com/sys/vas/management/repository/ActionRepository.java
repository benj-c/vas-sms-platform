package com.sys.vas.management.repository;

import com.sys.vas.management.dto.ActionByServiceDto;
import com.sys.vas.management.dto.entity.ActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActionRepository extends JpaRepository<ActionEntity, Long> {

    @Query("select new com.sys.vas.management.dto.ActionByServiceDto(a.id, a.description, a.api.id) from ActionEntity a where a.service.id = :id")
    List<ActionByServiceDto> findByServiceId(long id);
}
