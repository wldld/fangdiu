package com.gigaiot.nlostserver;


import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created by zz on 2017/6/6.
 */

public class NlostError {

    public static final int INVALID_SESSION = 1;
    public static final int INVALID_INPUT = 2;
    public static final int ACCESS_DENIED = 3;
    public static final int INVALID_USER_OR_PSD = 4;
    public static final int ERROR_PERFORM_REQUEST = 5;
    public static final int UNKNOW_ERROR = 6;
    public static final int PROCESS_ERROR = 7;
//    public static final int USERNAME_EMAIL_MISMATCH = 8;
    public static final int NO_SUCH_EMAIL = 8;
    public static final int DOUBLE_OBJECT = 9;
    public static final int INEXISTENT_OBJECT = 10;
    public static final int SEND_EMAIL_FAILED = 1001;
    public static final int INVALID_VERIFYCODE = 1002;
    public static final int EMAIL_HAS_BEEN_REGISTERED = 1003;

    private int error;
    public NlostError(int e) {
        this.error = e;
    }

    public String toString() {
        return "{\"error\":"+String.valueOf(error) +"}";
    }
}
