package com.sys.vas.datamodel.repository;

import com.sys.vas.datamodel.entity.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {

    Set<KeywordEntity> findByFirstKey(String fKey);
}
