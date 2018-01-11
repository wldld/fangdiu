package com.gigaiot.nlostserver.entity;

import lombok.Data;

import javax.persistence.*;

import com.gigaiot.nlostserver.entity.User;
/**
 * Created by cxm on 2017/9/30.
 */
@Entity(name = "t_silentperiod")
@Data
public class SilentPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int beginTime;
    private int endTime;
    private String days;
    private byte onOrOff;

    @ManyToOne
    private User user;
}
