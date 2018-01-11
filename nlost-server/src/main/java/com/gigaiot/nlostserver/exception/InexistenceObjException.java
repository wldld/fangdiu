package com.gigaiot.nlostserver.exception;

import com.gigaiot.nlostserver.NlostError;

/**
 * Created by guobaolin on 2017/7/12.
 */
public class InexistenceObjException extends NlostException {

    public InexistenceObjException(String message) {
        super(message, NlostError.INEXISTENT_OBJECT);
    }
}
