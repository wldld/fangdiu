package com.gigaiot.nlostserver.dto.emailDto;

import lombok.Data;

/**
 * Created by cxm on 2017/9/21.
 */
@Data
public class GetVerifyCodeReqDto {
    String email;
    String userName;
}
