package com.gigaiot.nlostserver.entity;

import lombok.Data;

import javax.persistence.Entity;

/**
 * Created by cxm on 2017/9/26.
 */
@Entity(name = "t_unit")
@Data
public class Unit extends Item {
    private String mac;
//    private String name;
    private String photoPath;
    private long lastActiveTime;
    private double lat;
    private double lon;
    private String macName;
}
