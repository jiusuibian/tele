package com.yd.telescope.system.repository;

import com.yd.telescope.common.repository.BaseRepository;
import com.yd.telescope.system.domain.Role;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface RoleRepository extends BaseRepository<Role,String>{

    @Query(value = "select * from ts_system_role t where t.role_id in ?1", nativeQuery = true)
    Set<Role> findByRole_ids(List<String> role_ids);

//    Role findByRole_id(String role_id);

    @Modifying
    @Transactional
    @Query(value = "update ts_system_role r set r.role_name = :role_name,r.remark = :remark, " +
            "r.modify_time = :modify_time,r.modifier = :modifier where r.role_id = :role_id", nativeQuery = true)
    void update(@Param("role_name") String role_name, @Param("remark") String remark,
                @Param("modify_time") Timestamp modify_time, @Param("modifier") String modifier, @Param("role_id") String role_id);
}
