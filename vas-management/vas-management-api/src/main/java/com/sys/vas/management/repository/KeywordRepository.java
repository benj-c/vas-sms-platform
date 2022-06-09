package com.sys.vas.management.repository;

import com.sys.vas.management.dto.KeywordDto;
import com.sys.vas.management.dto.entity.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {

    Set<KeywordEntity> findByFirstKey(String fKey);

    @Query("select new com.sys.vas.management.dto.KeywordDto(a.id, a.firstKey, a.regEx) from KeywordEntity a where a.action.id = :id")
    List<KeywordDto> findByActionId(Long id);
}
