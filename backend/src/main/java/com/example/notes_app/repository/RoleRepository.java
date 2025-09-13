package com.example.notes_app.repository;

import com.example.notes_app.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    boolean existsByName(String name);
    @Query("SELECT r FROM RoleEntity r JOIN r.accounts a WHERE a.id = :accountId")
    List<RoleEntity> findByAccountId(@Param("accountId") Integer accountId);

    Optional<RoleEntity> findByName(String roleUser);
}
