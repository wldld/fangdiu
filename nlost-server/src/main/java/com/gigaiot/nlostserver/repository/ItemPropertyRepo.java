package com.gigaiot.nlostserver.repository;

import com.gigaiot.nlostserver.entity.ItemProperty;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zz on 2017/5/28.
 */
public interface ItemPropertyRepo extends CrudRepository<ItemProperty, Integer> {

    @Transactional
    @Modifying
    @Query(value = "update t_item_property p set p.v=?1 where p.id=?2")
    int setVFor(String value, int id);

    @Transactional
    @Modifying
    @Query(value = "update t_item_property p set p.v=?1 where p.k=?2 and p.item.id=?3")
    int setVFor(String value, String name, String id);

    ItemProperty findByKAndItem_Id(String k, Integer itemId);

}
