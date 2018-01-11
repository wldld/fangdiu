package com.gigaiot.nlostserver.dto.settingdto;

import lombok.Data;

/**
 * Created by cxm on 2017/10/18.
 */
@Data
public class SubmitFeedbackReqDto {
    private String content;
    private String email;
}
