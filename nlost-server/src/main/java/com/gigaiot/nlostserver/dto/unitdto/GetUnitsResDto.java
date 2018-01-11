package com.gigaiot.nlostserver.dto.unitdto;

import lombok.Data;

/**
 * Created by cxm on 2017/9/28.
 */
@Data
public class GetUnitsResDto {
    int unitId;
    String mac;
    String name;
    String photo;
    double lat;
    double lon;
    long lastActiveTime;
    String macName;
}
