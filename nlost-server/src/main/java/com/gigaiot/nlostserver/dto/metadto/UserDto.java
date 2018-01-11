package com.gigaiot.nlostserver.dto.metadto;

import lombok.Data;

import java.util.Map;

/**
 * Created by zz on 2017/6/8.
 */

@Data
public class UserDto {

    private int id;

    private String name;
    private String photoPath;
    private String email;
    private String phone;
    private String pwd;
}
