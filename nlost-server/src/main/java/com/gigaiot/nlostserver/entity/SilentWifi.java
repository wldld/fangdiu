package com.gigaiot.nlostserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by cxm on 2017/10/16.
 */
@Entity(name = "t_silentwifi")
@Data
@JsonIgnoreProperties("user")
public class SilentWifi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String bssid;
    private String ssid;
    private byte onOrOff;

    @ManyToOne
    private User user;
}
