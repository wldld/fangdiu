package com.gigaiot.nlostserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigaiot.nlostserver.dto.corerequestdto.SingleParamRequestDto;
import com.gigaiot.nlostserver.dto.settingdto.GetSilentResDto;
import com.gigaiot.nlostserver.dto.settingdto.UpdateSettingsReqDto;
import com.gigaiot.nlostserver.service.SettingService;
import com.gigaiot.nlostserver.service.UserService;
import com.gigaiot.nlostserver.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by cxm on 2017/9/27.
 */
@RestController
@RequestMapping("/nlost/ajax.html")
public class SettingController {

    @Autowired
    SettingService settingService;
    @Autowired
    ObjectMapper om;

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/addNoDisturbingTime")
    public String addNoDisturbingTime(HttpServletRequest req, String sid, String params) throws Exception {
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return settingService.addNoDisturbingTime(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/getNoDisturbingTimeList")
    public String getNoDisturbingTimeList(HttpServletRequest req, String sid) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return settingService.getNoDisturbingTimeList(userId);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/setNoDisturbingTime")
    public String setNoDisturbingTime(HttpServletRequest req, String sid, String params) throws Exception {
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
        String time = requestDto.getData();
        return settingService.setNoDisturbingTime(userId, time);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/getNoDisturbingTime")
    public String getNoDisturbingTime(HttpServletRequest req, String sid) throws Exception {
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return settingService.getNoDisturbingTime(userId);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/deleteNoDisturbingTime")
    public String deleteNoDisturbingTime(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        return settingService.deleteNoDisturbingTime(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/addPeriod")
    public String addPeriod(HttpServletRequest req, String sid, String params) throws Exception {
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return settingService.addPeriod(Integer.valueOf(userId), params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/getPeriodList")
    public String getPeriodList(HttpServletRequest req, String sid) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return settingService.getPeriodList(Integer.valueOf(userId),(byte)0,false);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/findPeriodsByOnOrOff")
    public String findPeriodsByOnOrOff(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        Map<String, Integer> reqDto = om.readValue(params, Map.class);
        int onOrOff = reqDto.get("onOrOff");
        byte byteOnOrOff = Byte.parseByte("" + onOrOff);
//        if (onOrOff == 1) {
//            byteOnOrOff = (byte)1;
//        }else{
//            byteOnOrOff = (byte) 0;
//        }
        return settingService.getPeriodList(userId, byteOnOrOff, true);
//        return null;
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/setPeriodSwitch")
    public String setPeriodSwitch(HttpServletRequest req, String sid, String params) throws Exception{
        return settingService.setPeriodSwitch(params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/deletePeriod")
    public String deletePeriod(HttpServletRequest req, String sid, String params) throws Exception{
        return settingService.deletePeriod(params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/addSilentArea")
    public String addSilentArea(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        return settingService.addSilentArea(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/getSilentAreaList")
    public String getSilentAreaList(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        return settingService.getSilentAreaList(userId, (byte)0, false);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/findSilentAreasByOnOrOff")
    public String findSilentAreasByOnOrOff(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        Map<String, Byte> reqDto = om.readValue(params, Map.class);
        byte onOrOff = Byte.parseByte("" + reqDto.get("onOrOff"));
        return settingService.getSilentAreaList(userId, onOrOff, true);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/setSilentAreaSwitch")
    public String setSilentAreaSwitch(HttpServletRequest req, String sid, String params) throws Exception{
        return settingService.setSilentAreaSwitch(params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/deleteSilentArea")
    public String deleteSilentArea(HttpServletRequest req, String sid, String params) throws Exception{
        return settingService.deleteSilentArea(params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/addSilentWifi")
    public String addSilentWifi(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        return settingService.addSilentWifi(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/getSilentWifiList")
    public String getSilentWifiList(HttpServletRequest req, String sid) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        return settingService.getSilentWifiList(userId, (byte)0, false);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/findSilentWifiByIsOpen")
    public String findSilentWifiByIsOpen(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        Map<String, Integer> reqDto = om.readValue(params, Map.class);
        byte isOpen = Byte.valueOf("" + reqDto.get("onOrOff"));
        return settingService.getSilentWifiList(userId, isOpen, true);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/setSilentWifiSwitch")
    public String setSilentWifiSwitch(HttpServletRequest req, String sid, String params) throws Exception{
        return settingService.setSilentWifiSwitch(params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/submitFeedback")
    public String submitFeedback(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        return settingService.submitFeedback(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/getSilent")
    public String getSilent(HttpServletRequest req, String sid) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        String silentPeriod = settingService.getPeriodList(userId, (byte) 0, false);
        String silentWifi = settingService.getSilentWifiList(userId, (byte) 0, false);
        String silentArea = settingService.getSilentAreaList(userId, (byte) 0, false);
        GetSilentResDto resDto = new GetSilentResDto();
        resDto.setSilentArea(silentArea);
        resDto.setSilentPeriod(silentPeriod);
        resDto.setSilentWifi(silentWifi);
        return om.writeValueAsString(resDto);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/updateSettings")
    public String updateSettings(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return settingService.updateSettings(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/updatePeriods")
    public String updatePeriods(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return settingService.updatePeriods(Integer.valueOf(userId), params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/updateSilentAreas")
    public String updateSilentAreas(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return settingService.updateSilentAreas(Integer.valueOf(userId), params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=setting/updateNoDisturbingTimeList")
    public String updateNoDisturbingTimeList(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        return settingService.updateNoDisturbingTimeList(userId, params);
    }

}
