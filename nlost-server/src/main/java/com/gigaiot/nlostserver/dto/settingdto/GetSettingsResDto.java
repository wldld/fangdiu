package com.gigaiot.nlostserver.dto.settingdto;

import lombok.Data;

/**
 * Created by cxm on 2017/10/10.
 */
@Data
public class GetSettingsResDto {
    private String userName;
    private String email;
    private String photoPath;
    private int noDisturbingTime;
    private String repeatAlert;
    private String reconnectInform;
}
