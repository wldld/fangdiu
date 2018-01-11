package com.gigaiot.nlostserver.exception;

import com.gigaiot.nlostserver.NlostError;

/**
 * Created by zz on 2017/6/11.
 */
public class UnknownErrorException extends NlostException {

    public UnknownErrorException(String message) {
        super(message, NlostError.UNKNOW_ERROR);
    }
}
