package com.gigaiot.nlostserver.entity;

import lombok.Data;

/**
 * Created by cxm on 2017/12/6.
 */
@Data
public class TpLoginReqDto {
    private String type;
    private String username;
    private String id;
    private String icon;
    private String token;
}
