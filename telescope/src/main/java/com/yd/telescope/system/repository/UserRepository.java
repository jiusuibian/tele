package com.yd.telescope.system.repository;

import com.yd.telescope.common.repository.BaseRepository;
import com.yd.telescope.system.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public interface UserRepository extends BaseRepository<User, String> {

    User findByUsername(String username);
    @Modifying
    @Transactional
    @Query(value = "update ts_system_user u set u.password = :password,u.status = :status," +
            "u.userdesc = :userdesc,u.modify_time = :modify_time,u.modifier = :modifier where u.username = :username", nativeQuery = true)
    void update(@Param("username") String username, @Param("password") String password, @Param("status") String status,
                @Param("userdesc") String userdesc, @Param("modify_time") Timestamp modify_time, @Param("modifier") String modifier);

}
