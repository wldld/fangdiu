package com.gigaiot.nlostserver.dto.userdto;

import lombok.Data;

/**
 * Created by cxm on 2017/10/10.
 */
@Data
public class UpdatePasswordReqDto {
    private String oldPassword;
    private String newPassword;
}
