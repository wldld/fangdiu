package com.gigaiot.nlostserver.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * Created by zz on 2017/5/28.
 */

@Data
@Entity(name = "t_item_property")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"k", "item_id"})})
@NoArgsConstructor
@RequiredArgsConstructor
public class ItemProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "k")
    @NonNull
    private String k;

    @Column(name = "v")
    @NonNull
    private String v;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Item item;
}
