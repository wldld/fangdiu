package com.gigaiot.nlostserver.entity;

import lombok.Data;

/**
 * Created by cxm on 2017/12/27.
 */
@Data
public class UpdateUnitListReqDto {
    int unitId;
    String name;
    double lat;
    double lon;
    long lastActiveTime;
}
