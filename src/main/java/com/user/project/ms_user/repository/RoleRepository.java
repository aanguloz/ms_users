package com.user.project.ms_user.repository;

import com.user.project.ms_user.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = """
    select r.id, r.name, m.id, m.code, m.name, m.description
    from public.role_modules rm
    left join users.role r on r.id = rm.role_id
    left join navigate.module m on m.id = rm.module_id
    """, nativeQuery = true)
    List<Object[]> listRoles();
}
