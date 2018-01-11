package com.gigaiot.nlostserver.dto.unitdto;

import lombok.Data;

/**
 * Created by cxm on 2017/9/29.
 */
@Data
public class GetLocationDescriptionReqDto {
    int unitId;
    double lat;
    double lon;
}
