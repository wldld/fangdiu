package com.gigaiot.nlostserver.exception;

import com.gigaiot.nlostserver.NlostError;

/**
 * Created by zz on 2017/6/12.
 */
public class ErrorPerformingRequestException extends NlostException {

    public ErrorPerformingRequestException(String message) {
        super(message, NlostError.PROCESS_ERROR);
    }
}
