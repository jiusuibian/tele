package com.yd.telescope.system.repository;

import com.yd.telescope.common.repository.BaseRepository;
import com.yd.telescope.system.domain.Menu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface MenuRepository extends BaseRepository<Menu,Long>{

    @Query(value = "select * from ts_system_menu t where t.menu_id in ?1", nativeQuery = true)
    Set<Menu> findByMenu_ids(List<Integer> menu_ids);

    @Modifying
    @Transactional
    @Query(value = "update ts_system_menu m set m.name = :name,m.url = :url,m.perms = :perms," +
            "m.icon = :icon,m.modify_time = :modify_time,m.modifier = :modifier where m.menu_id = :menu_id", nativeQuery = true)
    void update(@Param("menu_id") long menu_id, @Param("name") String name, @Param("url") String url,@Param("perms") String perms,
                @Param("icon") String icon, @Param("modify_time") Timestamp modify_time, @Param("modifier") String modifier);
}
