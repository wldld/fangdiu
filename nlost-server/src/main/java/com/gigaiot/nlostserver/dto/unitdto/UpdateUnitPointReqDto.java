package com.gigaiot.nlostserver.dto.unitdto;

import lombok.Data;

/**
 * Created by cxm on 2017/9/27.
 */
@Data
public class UpdateUnitPointReqDto {
    int unitId;
    double lat;
    double lon;
}
