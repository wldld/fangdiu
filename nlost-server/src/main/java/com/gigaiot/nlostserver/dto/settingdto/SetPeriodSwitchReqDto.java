package com.gigaiot.nlostserver.dto.settingdto;

import lombok.Data;

/**
 * Created by cxm on 2017/10/12.
 */
@Data
public class SetPeriodSwitchReqDto {
    private int periodId;
    private byte periodSwitch;
}
