package com.sys.vas.management.repository;

import com.sys.vas.management.dto.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Modifying
    @Query("update UserEntity u set u.enabled = :isEnabled where u.id = :uid")
    void actDeactUser(
            @Param("isEnabled") boolean isEnabled,
            @Param("uid") int uid
    );

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(int id);
}
