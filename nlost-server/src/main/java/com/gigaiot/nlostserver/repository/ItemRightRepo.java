package com.gigaiot.nlostserver.repository;

import com.gigaiot.nlostserver.entity.Item;
import com.gigaiot.nlostserver.entity.ItemRight;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zz on 2017/6/12.
 */
public interface ItemRightRepo extends CrudRepository<ItemRight, Integer> {


    @Transactional
    @Modifying
    @Query(value = "update t_right r set r.value = ?1 where r.userId = ?2 and r.item.id = ?3")
    int setAccessFor(long v, int userId, int itemId);

    @Transactional
    @Modifying
    @Query(value = "delete from t_right r where r.userId = ?1 and item_id = ?2")
    int deleteByUserIdAndItemId(int userId, int itemId);


    @Transactional
    @Modifying
    @Query(value = "delete from t_right r where r.userId = ?1")
    int deleteByUserId(int userId);


    @Transactional
    @Modifying
    @Query(value = "delete from t_right r where r.userId in ?1")
    int deleteByUserIds(List<Integer> userIds);

    //获取user的拥有的item列表
    @EntityGraph(value = "ItemRight.detail")
    List<ItemRight> findByUserIdAndItem_clsAndItem_nameLike(int userId, int cls, String name, Sort sort);


    @EntityGraph(value = "ItemRight.detail")
    List<ItemRight> findByUserIdAndItem_clsAndItem_creatorNameLike(int userId, int cls, String creatorName, Sort sort);



    @EntityGraph(value = "ItemRight.detail")
    ItemRight findByUserIdAndItem_id(int userId, int itemId);


    @EntityGraph(value = "ItemRight.detail")
    List<ItemRight> findByUserId(int userId);

    //获取 item 的拥有者的 userd 列表
    @EntityGraph(value = "ItemRight.detail")
    List<ItemRight> findByItem_id(int itemId);

    @EntityGraph(value = "ItemRight.detail")
    List<ItemRight> findByUserIdAndItem_cls(int userId, int cls);

//
//    List<ItemRight> findByUserIdAndItem_cls(int userId, int clas);
//
//    sadfg


}
