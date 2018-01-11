package com.gigaiot.nlostserver.repository;

import com.gigaiot.nlostserver.entity.SilentPeriod;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cxm on 2017/10/10.
 */
public interface SilentPeriodRepo extends CrudRepository<SilentPeriod, Integer> {
    List<SilentPeriod> findByUser_Id(int userId);

    @Query(value = "select * from t_silentperiod p where p.onOrOff=?1 and p.user_id =?2 ", nativeQuery = true)
    List<SilentPeriod> findByOnOrOffAndUser_Id(byte onOrOff, int userId);

//    List<SilentPeriod> findByUser_IdAndOnOrOff(int userId, byte onOrOff);

    @Transactional
    @Modifying
    @Query(value = "update t_silentperiod s set s.onOrOff = ?1 where s.id=?2")
    int setPeriodSwitch(byte periodSwitch, int periodId);

    @Transactional
    int deleteByUser_Id(int userId);

}
