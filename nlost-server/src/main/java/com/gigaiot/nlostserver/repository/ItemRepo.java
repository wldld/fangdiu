package com.gigaiot.nlostserver.repository;

import com.gigaiot.nlostserver.entity.Item;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zz on 2017/5/28.
 */
public interface ItemRepo extends JpaRepository<Item, Integer> {

    // @Cacheable("ByNm")
    List<Item> findByName(String name);

    @EntityGraph(value = "Item.detail")
    Item findById(int id);

    int countByCreatorsLike(String creator);

    int countByNameAndCls(String name, int cls);

    @Transactional
    @Modifying
    @Query(value = "update t_item i set i.name = ?1 where i.id=?2")
    int updateNameById(String name, Integer id);

}
