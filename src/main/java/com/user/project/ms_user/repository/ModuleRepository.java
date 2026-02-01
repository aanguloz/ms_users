package com.user.project.ms_user.repository;

import com.user.project.ms_user.model.entity.Module;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module,Long> {

    @Query(value = """
    select m.id, m.code, m.name, m.description
    from navigate.module m
    left join public.role_modules rm on m.id = rm.module_id
    left join users.role r on r.id = rm.role_id
    where (
        :filter is null or :filter = ''
        or lower(m.code) like lower(concat('%', :filter, '%') )
        or lower(m.name) like lower(concat('%', :filter, '%') )
        or lower(r.name) like lower(concat('%', :filter, '%') )
    )
    """, nativeQuery = true)
    List<Object[]> searchModules(@Param("filter") String filter);

}
