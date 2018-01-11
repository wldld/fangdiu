package com.gigaiot.nlostserver.dto.settingdto;

import lombok.Data;

/**
 * Created by cxm on 2017/10/13.
 */
@Data
public class AddSilentAreaReqDto {
    private String name;
    private double circleCenterLat;
    private double circleCenterLon;
    private double radius;
    private byte onOrOff;
}
