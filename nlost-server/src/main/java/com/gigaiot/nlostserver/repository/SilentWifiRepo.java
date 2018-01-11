package com.gigaiot.nlostserver.repository;

import com.gigaiot.nlostserver.entity.SilentWifi;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cxm on 2017/10/16.
 */
public interface SilentWifiRepo extends CrudRepository<SilentWifi, Integer> {

    List<SilentWifi> findByUser_Id(int userId);

    @Query(value = "select * from t_silentwifi w where w.user_id = ?1 and w.onOroff=?2 ", nativeQuery = true)
    List<SilentWifi> findByUser_IdAndOnOrOff(int userId, byte isOpen);

    List<SilentWifi> findBySsidAndBssidAndUser_Id(String ssid, String bssid, int userId );

    @Transactional
    @Modifying
    @Query(value = "update t_silentwifi w set w.onOrOff = ?1 where w.id=?2", nativeQuery = true)
    int setSilentWifiSwitch(byte isOpen, int wifiId);

//    @Transactional
//    @Modifying
//    @Query(value = "delete from t_silentwifi w where w.user_id = ?1", nativeQuery = true)
//    int deleteByUser_Id(int userId);
}
