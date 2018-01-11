package com.gigaiot.nlostserver.exception;

import com.gigaiot.nlostserver.NlostError;

/**
 * Created by zz on 2017/6/11.
 */
public class DuplicateObjException extends NlostException {

    public DuplicateObjException(String message) {
        super(message, NlostError.DOUBLE_OBJECT);
    }
}
