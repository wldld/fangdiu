package com.gigaiot.nlostserver.dto.userdto;

import lombok.Data;

/**
 * Created by cxm on 2017/10/10.
 */
@Data
public class UpdateEmailReqDto {
    private String email;
    private String verifyCode;
}
