package com.gigaiot.nlostserver.dto.settingdto;

import lombok.Data;

/**
 * Created by cxm on 2017/10/16.
 */
@Data
public class SetSilentAreaSwitchReqDto {
    private int silentAreaId;
    private byte onOrOff;
}
