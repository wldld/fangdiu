package com.gigaiot.nlostserver.dto.settingdto;

import lombok.Data;

/**
 * Created by cxm on 2017/11/22.
 */
@Data
public class UpdateSettingsReqDto {
    int noDisturbingTime;
    String repeatAlert;
    String reconnectInform;
}
