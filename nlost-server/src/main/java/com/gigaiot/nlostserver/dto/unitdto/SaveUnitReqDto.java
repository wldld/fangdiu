package com.gigaiot.nlostserver.dto.unitdto;

import lombok.Data;

/**
 * Created by cxm on 2017/9/26.
 */
@Data
public class SaveUnitReqDto {
    private String macName;
    private String mac;
    private String name;
    private double lat;
    private double lon;
}
