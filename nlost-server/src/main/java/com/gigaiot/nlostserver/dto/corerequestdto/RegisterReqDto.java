package com.gigaiot.nlostserver.dto.corerequestdto;

import lombok.Data;

/**
 * Created by cxm on 2017/9/19.
 */
@Data
public class RegisterReqDto {
    private String userName;
    private String password;
    private String email;
    private int verifyCode;
}
