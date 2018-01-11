package com.gigaiot.nlostserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigaiot.nlostserver.dto.corerequestdto.SingleParamRequestDto;
import com.gigaiot.nlostserver.service.UserService;
import com.gigaiot.nlostserver.session.Session;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by cxm on 2017/9/25.
 */
@Slf4j
@RestController
@RequestMapping("/nlost/ajax.html")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    ObjectMapper om;

    @RequestMapping(method = RequestMethod.POST, params = "svc=user/resetPassword")
    public String resetPassword(HttpServletRequest req, String params) throws Exception{
        log.info("=====request:" + req.getParameter("svc"));
        String result = userService.resetPassword(params);
        log.info(result);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=user/editUserName")
    public String editUserName(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return userService.editUserName(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=user/editPhone")
    public String editPhone(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return userService.editPhone(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=user/getUser")
    public String getUser(HttpServletRequest req, String sid) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return userService.getUser(userId);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=user/updateEmail")
    public String updateEmail(HttpServletRequest req, String sid, String params) throws Exception {
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return userService.updateEmail(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=user/updatePassword")
    public String updatePassword(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return userService.updatePassword(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=user/setRepeatAlert")
    public String setRepeatAlert(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
        String onOrOff = requestDto.getData();
        return userService.setRepeatAlert(userId, onOrOff);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=user/setReconnectInform")
    public String setReconnectInform(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
        String onOrOff = requestDto.getData();
        return userService.setReconnectInform(userId, onOrOff);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=user/getSettings")
    public String getSettings(HttpServletRequest req, String sid) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return userService.getSettings(userId);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=user/updateUser")
    public String updateUser(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        return userService.updateUser(userId, params);
    }

}
