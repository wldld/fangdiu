package com.gigaiot.nlostserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigaiot.nlostserver.NlostError;
import com.gigaiot.nlostserver.NlostServer;
import com.gigaiot.nlostserver.dto.corerequestdto.LoginRequestDto;
import com.gigaiot.nlostserver.dto.corerequestdto.SingleParamRequestDto;
import com.gigaiot.nlostserver.dto.corerequestdto.VerifyEmailReqDto;
import com.gigaiot.nlostserver.repository.UserRepo;
import com.gigaiot.nlostserver.service.CoreService;
import com.gigaiot.nlostserver.session.Session;
import com.gigaiot.nlostserver.session.SessionService;
import com.gigaiot.nlostserver.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zz on 2017/6/5.
 */
@Slf4j
@Controller
@RequestMapping("/nlost/ajax.html")
public class Ajax {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CoreService core;
//
//    @Autowired
//    private UnitService unit;
//
//    @Autowired
//    private ItemService item;
//
//    @Autowired
//    private UserService userName;
//
//    @Autowired
//    private AccountService account;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserRepo userRepo;

    /**
     * login
     *
     * @param req    request Obj <br />
     * @param params 参数: {"userName": "name", "password": "password" } <br />
     * @return {"host": "*.*.*.*", "eid": "session unitId", "au": "account", "tm": timestamp(ms),
     * "userName": {"nm":"name","cls":1,"unitId":"1", "prp":{"property..."}, "crt":"","bact":"1","fl":255,"uacl":2146947}}
     * @throws IOException 获取参数对象(LoginRequestDto)时或可能产生的错误
     */
    @RequestMapping(method = RequestMethod.POST, params ="svc=core/login")
    @ResponseBody
    public String login(HttpServletRequest req, String params) throws Exception{
        log.info("=====request:" + req.getParameter("svc"));
        LoginRequestDto reqDto = om.readValue(params, LoginRequestDto.class);
        String result = core.login(req.getRemoteHost(), reqDto);
        log.info("-----response:" + result);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, params ="svc=core/logout")
    @ResponseBody
    public String logout(HttpServletRequest req, String sid, String params) {
        Session session = (Session) req.getAttribute("session");
        core.logout(session.getUserId(), session.getSid());
        return NlostServer.SUCCESS;
    }

    @RequestMapping(method = RequestMethod.POST, params ="svc=core/checkUserName")
    @ResponseBody
    public String checkUserName(HttpServletRequest req, String params) throws Exception{
        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
        String userName = requestDto.getData();
        boolean isAvailable = core.checkUserName(userName);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);
        log.info("=====request:" + req.getParameter("svc"));
        String result = om.writeValueAsString(response);
        log.info("-----response:" + result);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, params ="svc=core/getVerifyCode")
    @ResponseBody
    public String getVerifyCode(HttpServletRequest req, String params) throws Exception{
        log.info("=====request:" + req.getParameter("svc"));
        String result = core.getVerifyCode(params);
        log.info("-----response:" + result);
        return result;

    }

    @RequestMapping(method = RequestMethod.POST, params ="svc=core/register")
    @ResponseBody
    public String register(HttpServletRequest req, String params) throws Exception{
        log.info("=====request:" + req.getParameter("svc"));
        String result = core.register(params);
        log.info("-----response:" + result);
        return result;
    }


    @RequestMapping(method = RequestMethod.POST, params ="svc=core/sendVerifyUrl")
    @ResponseBody
    public String sendVerifyUrl(HttpServletRequest req, String params) throws Exception{
        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
        String email = requestDto.getData();
        log.info("=====request:" + req.getParameter("svc"));
        String result = core.sendVerifyUrl(email);
        log.info("-----response:" + result);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/verifyEmail")
    @ResponseBody
    public String verifyEmail(HttpServletRequest req, String key) throws Exception{
        String keyValue = ValidateUtils.getFromBase64(key);
        String[] keyArray = keyValue.split(",");
        String email = keyArray[0];
        String verifyCode = keyArray[1];
        log.info("=====request:" + req.getParameter("svc"));
        String result = core.verifyEmail(email, verifyCode);
        log.info("-----response:" + result);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=core/tpLogin")
    @ResponseBody
    public String tpLogin(HttpServletRequest req, String params) throws Exception{
        log.info("=====request:" + req.getParameter("svc"));
        String result = core.tpLogin(req.getRemoteHost(),params);
        log.info("-----response:" + result);
        return result;
    }


}
