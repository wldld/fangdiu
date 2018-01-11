package com.gigaiot.nlostserver.advice;

import com.gigaiot.nlostserver.exception.InvalidSessionException;
import com.gigaiot.nlostserver.session.Session;
import com.gigaiot.nlostserver.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zz on 2017/6/11.
 */

@Component
@Aspect
@Order(2)
@Slf4j
public class SessionHandler {

    @Autowired
    private SessionService ss;

    @Around("execution(* com.gigaiot.nlostserver.controller..*(..))" +
            "&& !execution(* com.gigaiot.nlostserver.controller..login(..))"+
            "&& !execution(* com.gigaiot.nlostserver.controller..getVerifyCode(..))" +
            "&& !execution(* com.gigaiot.nlostserver.controller..register(..))" +
            "&& !execution(* com.gigaiot.nlostserver.controller..checkUserName(..))" +
            "&& !execution(* com.gigaiot.nlostserver.controller..resetPassword(..))" +
            "&& !execution(* com.gigaiot.nlostserver.controller..sendVerifyUrl(..))" +
            "&& !execution(* com.gigaiot.nlostserver.controller..verifyEmail(..))" +
            "&& !execution(* com.gigaiot.nlostserver.controller..getMap(..))" +
            "&& !execution(* com.gigaiot.nlostserver.controller..tpLogin(..))"
    )
    public Object process(ProceedingJoinPoint point) throws Throwable {


        Object[] args = point.getArgs();

        HttpServletRequest req = (HttpServletRequest) args[0];
        log.info("=====request:" + req.getParameter("svc"));
        String sid = (String) args[1];
        Session session = ss.findBySid(sid);
        if (session == null) {
            throw  new InvalidSessionException("");
        }
        session.setHost(req.getRemoteHost());
        req.setAttribute("session", session);
        Object returnValue = point.proceed(args);
        log.info("-----response:" + returnValue);
        return returnValue;
    }

}
