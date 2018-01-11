package com.gigaiot.nlostserver.repository;

import com.gigaiot.nlostserver.entity.Unit;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by cxm on 2017/9/27.
 */
public interface UnitRepo extends CrudRepository<Unit, Integer> {

    Unit findByMac(String mac);

    List<Unit> findByMacName(String macName);

    Unit findById(int id);
//
    @Query(value = "select * from t_unit u, t_right r, t_item i where u.id = r.item_id and u.id = i.id and r.user_id = ?1 and i.cls = ?2 ", nativeQuery=true)
    List<Unit> findByUserIdAndItemCls(int userId, int itemCls);

    @Transactional
    @Modifying
    @Query(value = "update t_unit u set u.name=?2, u.lat=?3, u.lon=?4, u.lastActiveTime=?5 where u.id =?1")
    int updateById(int id, String name, double lat, double lon, long lastActiveTime);


}
