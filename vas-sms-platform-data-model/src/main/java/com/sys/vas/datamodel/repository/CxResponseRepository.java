package com.sys.vas.datamodel.repository;

import com.sys.vas.datamodel.entity.ApiEntity;
import com.sys.vas.datamodel.entity.CxResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CxResponseRepository extends JpaRepository<CxResponseEntity, Long> {

    List<CxResponseEntity> findByResCodeAndApi(int resCode, ApiEntity api);
}
