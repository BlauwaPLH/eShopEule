package org.senju.eshopeule.repository;

import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.model.user.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Optional<Permission> findByName(String name);

    @Query(value = "SELECT new org.senju.eshopeule.dto.PermissionDTO(p.id, p.name, p.description) FROM Permission AS p")
    List<PermissionDTO> getAllPerms();
}
