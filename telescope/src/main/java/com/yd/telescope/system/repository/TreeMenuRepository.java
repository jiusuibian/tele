package com.yd.telescope.system.repository;

import com.yd.telescope.common.repository.BaseRepository;
import com.yd.telescope.system.domain.TreeMenu;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TreeMenuRepository extends BaseRepository<TreeMenu,Long>{

    @Query(value = "select * from ts_system_menu t where t.parent_id = ?1", nativeQuery = true)
    List<TreeMenu> findByParent_id(long parent_id);
}
