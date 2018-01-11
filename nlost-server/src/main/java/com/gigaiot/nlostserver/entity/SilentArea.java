package com.gigaiot.nlostserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by cxm on 2017/10/12.
 */
@Entity(name = "t_silentarea")
@Data
@JsonIgnoreProperties("user")
public class SilentArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double circleCenterLat;
    private double circleCenterLon;
    private double radius;
    private byte onOrOff;

    @ManyToOne
    private User user;
}
