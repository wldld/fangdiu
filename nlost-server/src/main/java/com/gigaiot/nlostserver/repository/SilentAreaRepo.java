package com.gigaiot.nlostserver.repository;

import com.gigaiot.nlostserver.entity.SilentArea;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by cxm on 2017/10/16.
 */
public interface SilentAreaRepo extends CrudRepository<SilentArea, Integer> {

    List<SilentArea> findByUser_id(int userId);

    @Query(value = "select * from t_silentarea a where a.user_id =?1 and a.onoroff=?2 ", nativeQuery = true)
    List<SilentArea> findByUser_IdAndOnOrOff(int userId, byte onOrOff);

    @Transactional
    @Modifying
    @Query(value = "update t_silentarea area set area.onOrOff =?2 where area.id=?1 ")
    int setSilentAreaSwitch(int silentId, byte onOrOff);

    @Transactional
    int deleteByUser_Id(int userId);
}
