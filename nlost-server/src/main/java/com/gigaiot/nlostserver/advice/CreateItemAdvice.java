package com.gigaiot.nlostserver.advice;

import com.gigaiot.nlostserver.exception.UnknownErrorException;
import com.gigaiot.nlostserver.session.Session;
import com.gigaiot.nlostserver.session.SessionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zz on 2017/6/14.
 */
@Component
@Aspect
public class CreateItemAdvice {

    @Autowired
    private SessionService ss;

    @Around("execution(* com.gigaiot.nlostserver.service.CoreService.*create*(..))")
    public Object process(ProceedingJoinPoint point) throws Throwable {

        Object[] args = point.getArgs();
        Session session = (Session) args[0];

        boolean canCreateItem = canCreateItem(session);
        if (!canCreateItem) {
            throw  new UnknownErrorException("");
        }

        Object returnValue = point.proceed(args);
        return returnValue;
    }


    public boolean canCreateItem(Session session) {
        int fl = session.getUserFlags();
        return (fl & 0x4) !=0;

    }

}
