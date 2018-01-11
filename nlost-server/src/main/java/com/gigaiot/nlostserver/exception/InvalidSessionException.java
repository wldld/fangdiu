package com.gigaiot.nlostserver.exception;

import com.gigaiot.nlostserver.NlostError;

/**
 * Created by zz on 2017/6/11.
 */
public class InvalidSessionException extends NlostException {

    public InvalidSessionException(String message) {
        super(message, NlostError.INVALID_SESSION);
    }
}
