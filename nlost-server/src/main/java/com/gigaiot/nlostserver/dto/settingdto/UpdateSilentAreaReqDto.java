package com.gigaiot.nlostserver.dto.settingdto;

import lombok.Data;

/**
 * Created by cxm on 2017/11/23.
 */
@Data
public class UpdateSilentAreaReqDto {
    private long id;
    private String name;
    private double circleCenterLat;
    private double circleCenterLon;
    private double radius;
    private byte onOrOff;
}
