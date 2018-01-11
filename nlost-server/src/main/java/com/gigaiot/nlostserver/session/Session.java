package com.gigaiot.nlostserver.session;

import com.gigaiot.nlostserver.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Created by zz on 2017/6/10.
 */
@Data
@NoArgsConstructor
public class Session {

    private String sid;
    private String userId;
    private String host;
    private int userFlags;

    public Session(User user) {
        this.sid = UUID.randomUUID().toString().replace("-", "");
        this.userId = String .valueOf(user.getId());
        this.userFlags = user.getFlags();
    }

//    public Session(User userName, String host) {
//        this.sid = UUID.randomUUID().toString().replace("-", "");
//        this.userId = String .valueOf(userName.getUnitId());
//        this.userFlags = userName.getFlags();
//        this.host = host;
//    }
}
