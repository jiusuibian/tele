package com.yd.telescope.management.repository;

import com.yd.telescope.common.repository.BaseRepository;
import com.yd.telescope.management.domain.App;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface AppRepository extends BaseRepository<App,String> {

    App findByUsernameAndAppname(String username,String appname);

    @Modifying
    @Transactional
    @Query(value = "update ts_management_app t set t.status = :status,t.modify_time = :modify_time,t.modifier = :modifier where t.appid = :appid", nativeQuery = true)
    void update(@Param("appid") String appid, @Param("status") String status, @Param("modify_time") Timestamp modify_time, @Param("modifier") String modifier);

    List<App>  findByApptypeAndUsername(String apptype, String username);

    @Query(value = "SELECT * FROM ts_management_app t WHERE t.username = ?1 AND t.appname = ?2 AND t.ostype = ?3",nativeQuery = true)
    App exists(String username, String appname, String ostype);
}
