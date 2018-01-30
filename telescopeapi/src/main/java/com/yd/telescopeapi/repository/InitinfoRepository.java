package com.yd.telescopeapi.repository;

import com.yd.telescopeapi.domain.Initinfo;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.websocket.server.PathParam;

/**
 *    手机信息初始化
 *
 * @author zygong
 * @date 2017/12/22 13:03
 */
public interface InitinfoRepository extends JpaRepository<Initinfo, Long> {

    /**
     *    通过设备号查询设备
     *
     * @author zygong
     * @date 2017/12/22 16:45
     * @param [appId]
     * @return com.yd.telescopeapi.domain.Initinfo:
     */
    @Query("from Initinfo t where t.t_dev_id=:tDevId and t.t_app_id =:appId")
    public Initinfo getByDevIdAndAppId(@Param("tDevId") String tDevId, @Param("appId")String appId);

}
