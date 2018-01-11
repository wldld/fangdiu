package com.gigaiot.nlostserver.exception;

import lombok.Data;

/**
 * Created by zz on 2017/6/11.
 */
@Data
public class NlostException extends RuntimeException {

    private int errorCode;
    public NlostException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getJsonMessage() {
        return "{\"error\":"+String.valueOf(errorCode) +"}";
    }
}
