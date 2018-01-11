package com.gigaiot.nlostserver.dto.corerequestdto;

import lombok.Data;

/**
 * Created by cxm on 2017/11/13.
 */
@Data
public class VerifyEmailReqDto {
    private String email;
    private String verifyCode;
}
