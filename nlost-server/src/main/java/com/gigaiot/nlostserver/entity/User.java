package com.gigaiot.nlostserver.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Created by zz on 2017/5/25.
 */

@Entity(name = "t_user")
@Data
public class User extends Item {

    private int flags;  //二进制421，1表示enable或者disable，2表示能否修改密码，4表示能否创建设备。

    private long lastLoginTime;

    private String lastLoginHost;

    private String password;

    private String profilePhoto;

    private String email;

    private String phone;

    private String accountType;

    private String tpId; //third party id

}
