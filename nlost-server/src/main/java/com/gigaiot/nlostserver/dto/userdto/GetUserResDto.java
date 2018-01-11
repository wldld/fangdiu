package com.gigaiot.nlostserver.dto.userdto;

import lombok.Data;

/**
 * Created by cxm on 2017/10/10.
 */
@Data
public class GetUserResDto {
    private String name;
    private String photoPath;
    private String email;
    private String phone;
}
