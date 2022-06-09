package com.sys.vas.management.repository;

import com.sys.vas.management.dto.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {

    Optional<UserRoleEntity> findByRole(String role);
}
