package com.gigaiot.nlostserver.advice;

import com.gigaiot.nlostserver.exception.ErrorPerformingRequestException;
import com.gigaiot.nlostserver.exception.NlostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zz on 2017/6/11.
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody String handleControllerException(HttpServletRequest request, Throwable ex) {
        //System.out.println("handleControllerException " + ex.toString());
        log.error("",ex);
        return new ErrorPerformingRequestException("").getJsonMessage();
    }


    @ExceptionHandler(value = NlostException.class)
    @ResponseBody String handleNlostException(HttpServletRequest request, NlostException ex) {
        //System.out.println("handleIotException " + ex.toString());
        return ex.getJsonMessage();
    }
}



