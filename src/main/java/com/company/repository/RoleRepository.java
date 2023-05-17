package com.company.repository;

import com.company.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query(value ="select r.id, r.name,a.id, a.name from role r\n" +
            "join role_authority ra on r.id = ra.role_id\n" +
            "join authority a on a.id = ra.authority_id\n" +
            "where a.id =:authorityId",nativeQuery = true)
    Optional<Set<Role>> findRoleByAuthority(Long authorityId);
}
