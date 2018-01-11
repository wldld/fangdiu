package com.gigaiot.nlostserver.dto.unitdto;

import lombok.Data;

/**
 * Created by cxm on 2017/10/19.
 */
@Data
public class SaveUnitResDto {
    private int unitId;
    private String mac;
    private String macName;
    private String name;
    private String photo;
    private long lastActiveTime;
    private double lat;
    private double lon;
}
