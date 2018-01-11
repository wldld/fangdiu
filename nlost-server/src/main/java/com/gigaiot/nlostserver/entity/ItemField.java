package com.gigaiot.nlostserver.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by zz on 2017/6/7.
 */

@Data
@Entity(name = "t_item_field")
@NoArgsConstructor
public class ItemField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "n")
    private String n;

    @Column(name = "v")
    private String v;
}
