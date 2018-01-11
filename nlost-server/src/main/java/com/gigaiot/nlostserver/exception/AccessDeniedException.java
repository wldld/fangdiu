package com.gigaiot.nlostserver.exception;

import com.gigaiot.nlostserver.NlostError;

/**
 * Created by zz on 2017/6/11.
 */
public class AccessDeniedException extends NlostException {

    public AccessDeniedException(String message) {
        super(message, NlostError.ACCESS_DENIED);
    }
}
