package com.gigaiot.nlostserver.exception;

import com.gigaiot.nlostserver.NlostError;

/**
 * Created by zz on 2017/6/11.
 */
public class InvalidUserNameException extends NlostException {

    public InvalidUserNameException(String message) {
        super(message, NlostError.INVALID_USER_OR_PSD);
    }
}
