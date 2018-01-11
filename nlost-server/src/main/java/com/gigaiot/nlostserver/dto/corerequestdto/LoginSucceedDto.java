package com.gigaiot.nlostserver.dto.corerequestdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gigaiot.nlostserver.dto.metadto.UserDto;
import lombok.Data;

/**
 * Created by zz on 2017/6/6.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginSucceedDto {

//    private String host;
    private String sid;
//    private String userName;
//    private long time;
    private UserDto user;

}
