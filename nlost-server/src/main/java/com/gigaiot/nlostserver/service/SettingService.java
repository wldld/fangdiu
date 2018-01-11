package com.gigaiot.nlostserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigaiot.nlostserver.NlostServer;
import com.gigaiot.nlostserver.dto.corerequestdto.SingleIntParamReqDto;
import com.gigaiot.nlostserver.dto.corerequestdto.SingleParamRequestDto;
import com.gigaiot.nlostserver.dto.settingdto.*;
import com.gigaiot.nlostserver.entity.*;
import com.gigaiot.nlostserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cxm on 2017/9/27.
 */
@Service
public class SettingService {

    @Autowired
    ObjectMapper om;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ItemPropertyRepo itemPropertyRepo;
    @Autowired
    SilentPeriodRepo silentPeriodRepo;
    @Autowired
    SilentAreaRepo silentAreaRepo;
    @Autowired
    SilentWifiRepo silentWifiRepo;
    @Autowired
    FeedbackRepo feedbackRepo;
    @Autowired
    UserService userService;

    public String addNoDisturbingTime(String userId, String params) throws Exception{
        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
        String time = requestDto.getData();
        User user = userRepo.findById(Integer.valueOf(userId));

        ItemProperty noDisturbingTimeList = itemPropertyRepo.findByKAndItem_Id("noDisturbingTimeList", user.getId());
        if (noDisturbingTimeList == null) {
            noDisturbingTimeList = new ItemProperty();
            noDisturbingTimeList.setItem(user);
            noDisturbingTimeList.setK("noDisturbingTimeList");
            noDisturbingTimeList.setV(time);
        }else{
            if ("".equals(noDisturbingTimeList.getV())) {
                noDisturbingTimeList.setV(time);
            }else{
                String[] list = noDisturbingTimeList.getV().split(",");
                for (String str : list) {
                    if (time.equals(str)) {
                        return NlostServer.SUCCESS;
                    }
                }
                noDisturbingTimeList.setV(noDisturbingTimeList.getV() + "," + time);
            }
        }

        itemPropertyRepo.save(noDisturbingTimeList);
        return NlostServer.SUCCESS;
    }

    public String getNoDisturbingTimeList(String userId) throws Exception {
        ItemProperty timeListProperty = itemPropertyRepo.findByKAndItem_Id("noDisturbingTimeList", Integer.valueOf(userId));
        List<Integer> response = new ArrayList<>();
        if (timeListProperty != null) {
            String timeList = timeListProperty.getV();
            if(!"".equals(timeList)){
                String [] strList = timeList.split(",");
                for (String str : strList) {
                    response.add(Integer.valueOf(str));
                }
            }
        }
//        List<Integer> response = new ArrayList<>();
        return om.writeValueAsString(response);
    }

    public String setNoDisturbingTime(String userId, String time) throws Exception {
//        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
//        String time = requestDto.getData();
        User user = userRepo.findById(Integer.valueOf(userId));

        ItemProperty selectedNoDisturbingTime = itemPropertyRepo.findByKAndItem_Id("noDisturbingTimeSelect", Integer.valueOf(userId));
        if (selectedNoDisturbingTime == null) {
            selectedNoDisturbingTime = new ItemProperty();
            selectedNoDisturbingTime.setItem(user);
            selectedNoDisturbingTime.setK("noDisturbingTimeSelect");
            selectedNoDisturbingTime.setV(time);
        } else {
            selectedNoDisturbingTime.setV(time);
        }
        itemPropertyRepo.save(selectedNoDisturbingTime);
        return NlostServer.SUCCESS;
    }

    public String getNoDisturbingTime(String userId) throws Exception {
        ItemProperty noDisturbingTimeProperty = itemPropertyRepo.findByKAndItem_Id("noDisturbingTimeSelect", Integer.valueOf(userId));
        String noDisturbingTime = noDisturbingTimeProperty.getV();
        if (noDisturbingTime == null || "".equals(noDisturbingTime)) {
            noDisturbingTime = "60";
        }
        Map<String, Integer> response = new HashMap<>();
        response.put("noDisturbingTime", Integer.valueOf(noDisturbingTime));
        return om.writeValueAsString(response);
    }

    public String deleteNoDisturbingTime(int userId, String params) throws Exception{
        SingleIntParamReqDto reqDto = om.readValue(params, SingleIntParamReqDto.class);
        String deleteTime = String.valueOf(reqDto.getData());

        ItemProperty noDisturbingTimeListProperty = itemPropertyRepo.findByKAndItem_Id("noDisturbingTimeList", userId);
        String noDisturbingTimeList = noDisturbingTimeListProperty.getV();
        String[] noDisturbingTimeArray = noDisturbingTimeList.split(",");
        List<String> newTimeList = new ArrayList<>();
        for (String time : noDisturbingTimeArray) {
            if(!time.equals(deleteTime)){
                newTimeList.add(time);
            }
        }
        StringBuffer newTimeListStringBuffer = new StringBuffer("");
        for(int i=0; i<newTimeList.size();i++) {
            if (i == 0) {
                newTimeListStringBuffer.append(newTimeList.get(i));
            }else{
                newTimeListStringBuffer.append("," + newTimeList.get(i));
            }
        }

        ItemProperty noDisturbingTimeSelectProperty = itemPropertyRepo.findByKAndItem_Id("noDisturbingTimeSelect", userId);
        String noDisturbingTimeSelect = noDisturbingTimeSelectProperty.getV();
        String newNoDisturbingTimeSelect = "";
        if (!noDisturbingTimeSelect.equals(deleteTime)) {
             newNoDisturbingTimeSelect = noDisturbingTimeSelect;
        }

        noDisturbingTimeListProperty.setV(newTimeListStringBuffer.toString());
        itemPropertyRepo.save(noDisturbingTimeListProperty);
        noDisturbingTimeSelectProperty.setV(newNoDisturbingTimeSelect);
        itemPropertyRepo.save(noDisturbingTimeSelectProperty);

        return NlostServer.SUCCESS;
    }

    public String addPeriod(int userId, String params) throws Exception {
        AddPeriodReqDto reqDto = om.readValue(params, AddPeriodReqDto.class);
        int beginTime = reqDto.getBeginTime();
        int endTime = reqDto.getEndTime();
        List dates = reqDto.getDates();
        StringBuffer strDates = new StringBuffer("");
        for (int i = 0; i < dates.size(); i++) {
            if (i == 0) {
                strDates.append(dates.get(i));
            }else{
                strDates.append("," + dates.get(i));
            }
        }
        User user = userRepo.findById(userId);

        SilentPeriod silentPeriod = new SilentPeriod();
        silentPeriod.setBeginTime(beginTime);
        silentPeriod.setEndTime(endTime);
        silentPeriod.setDays(strDates.toString());
        silentPeriod.setOnOrOff((byte) 0);
        silentPeriod.setUser(user);
        silentPeriodRepo.save(silentPeriod);

        AddPeriodResDto response = new AddPeriodResDto();
        response.setId(silentPeriod.getId());
        response.setBeginTime(silentPeriod.getBeginTime());
        response.setEndTime(silentPeriod.getEndTime());
        response.setDates(dates);
        response.setOnOrOff(silentPeriod.getOnOrOff());

        return om.writeValueAsString(response);
    }

    public String getPeriodList(int userId, byte onOrOff, boolean isSelect) throws Exception{
        List<SilentPeriod> silentPeriodList = null;
        if (isSelect) {
            silentPeriodList = silentPeriodRepo.findByOnOrOffAndUser_Id(onOrOff, userId);
        }else{
            silentPeriodList = silentPeriodRepo.findByUser_Id(userId);
        }
        List<GetPeriodListResDto> resDtoList = silentPeriodList.stream().map(silentPeriod -> {
            GetPeriodListResDto resDto = new GetPeriodListResDto();
            resDto.setId(silentPeriod.getId());
            resDto.setBeginTime(silentPeriod.getBeginTime());
            resDto.setEndTime(silentPeriod.getEndTime());
            String[] dateArray = silentPeriod.getDays().split(",");
            List<Integer> dateList = new ArrayList<>();
            for (String date : dateArray) {
                if(!"".equals(date)){
                    dateList.add(Integer.valueOf(date));
                }
            }
            resDto.setDates(dateList);
            resDto.setOnOrOff(silentPeriod.getOnOrOff());
            return resDto;
        }).collect(Collectors.toList());
        return om.writeValueAsString(resDtoList);
    }


    public String deletePeriod(String params) throws Exception{
        SingleIntParamReqDto reqDto = om.readValue(params, SingleIntParamReqDto.class);
        int periodId = reqDto.getData();
        silentPeriodRepo.delete(periodId);
        return NlostServer.SUCCESS;
    }

    public String setPeriodSwitch(String params) throws Exception{
        SetPeriodSwitchReqDto reqDto = om.readValue(params, SetPeriodSwitchReqDto.class);
        int periodId = reqDto.getPeriodId();
        byte periodSwitch = reqDto.getPeriodSwitch();
        silentPeriodRepo.setPeriodSwitch(periodSwitch, periodId);
        return NlostServer.SUCCESS;
    }

    public String addSilentArea(int userId, String params) throws Exception {
        AddSilentAreaReqDto reqDto = om.readValue(params, AddSilentAreaReqDto.class);
        String name = reqDto.getName();
        double circleCenterLat = reqDto.getCircleCenterLat();
        double circleCenterLon = reqDto.getCircleCenterLon();
        double radius = reqDto.getRadius();
        byte onOrOff = reqDto.getOnOrOff();

        User user = userRepo.findById(userId);
        SilentArea silentArea = new SilentArea();
        silentArea.setName(name);
        silentArea.setCircleCenterLat(circleCenterLat);
        silentArea.setCircleCenterLon(circleCenterLon);
        silentArea.setRadius(radius);
        silentArea.setOnOrOff(onOrOff);
        silentArea.setUser(user);
        silentAreaRepo.save(silentArea);

        return om.writeValueAsString(silentArea);
    }

    public String getSilentAreaList(int userId, byte onOrOff, boolean isSelect) throws Exception{
        List<SilentArea> silentAreaList = null;
        if (isSelect) {
            silentAreaList = silentAreaRepo.findByUser_IdAndOnOrOff(userId, onOrOff);
        }else{
            silentAreaList = silentAreaRepo.findByUser_id(userId);
        }

        return om.writeValueAsString(silentAreaList);
    }

    public String setSilentAreaSwitch(String params) throws Exception{
        SetSilentAreaSwitchReqDto reqDto = om.readValue(params, SetSilentAreaSwitchReqDto.class);
        silentAreaRepo.setSilentAreaSwitch(reqDto.getSilentAreaId(), reqDto.getOnOrOff());
        return NlostServer.SUCCESS;
    }

    public String deleteSilentArea(String params) throws Exception{
        Map<String, Integer> reqDto = om.readValue(params, Map.class);
        int silentAreaId = reqDto.get("silentAreaId");
        silentAreaRepo.delete(silentAreaId);
        return NlostServer.SUCCESS;
    }

    public String addSilentWifi(int userId, String params) throws Exception{
        AddSilentWifiReqDto reqDto = om.readValue(params, AddSilentWifiReqDto.class);
        User user = userRepo.findById(userId);
        List<SilentWifi> silentWifiList = silentWifiRepo.findBySsidAndBssidAndUser_Id(reqDto.getSsid(), reqDto.getBssid(), user.getId());
        if (silentWifiList != null && silentWifiList.size() > 0) {
            return NlostServer.SUCCESS;
        }
        SilentWifi silentWifi = new SilentWifi();
        silentWifi.setBssid(reqDto.getBssid());
        silentWifi.setSsid(reqDto.getSsid());
        silentWifi.setOnOrOff(reqDto.getOnOrOff());
        silentWifi.setUser(user);
        silentWifiRepo.save(silentWifi);
        return NlostServer.SUCCESS;
    }

    public String getSilentWifiList(int userId, byte isOpen, boolean isSelect) throws Exception{
        List<SilentWifi> silentWifiList = null;
        if (isSelect) {
            silentWifiList = silentWifiRepo.findByUser_IdAndOnOrOff(userId, isOpen);
        }else {
            silentWifiList = silentWifiRepo.findByUser_Id(userId);
        }

        return om.writeValueAsString(silentWifiList);
    }

    public String setSilentWifiSwitch(String params) throws Exception{
        SetSilentWifiSwitchReqDto reqDto = om.readValue(params, SetSilentWifiSwitchReqDto.class);
        byte isopen = reqDto.getOnOrOff();
        int wifiId = reqDto.getWifiId();
        silentWifiRepo.setSilentWifiSwitch(reqDto.getOnOrOff(), reqDto.getWifiId());
        return NlostServer.SUCCESS;
    }

    public String submitFeedback(int userId, String params) throws Exception{
        SubmitFeedbackReqDto reqDto = om.readValue(params, SubmitFeedbackReqDto.class);
        String content = reqDto.getContent();
        String email = reqDto.getEmail();
        User user = new User();
        user.setId(userId);

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setEmail(email);
        feedback.setContent(content);

        feedbackRepo.save(feedback);
        return NlostServer.SUCCESS;
    }

    public String updateSettings(String userId, String params) throws Exception{
        UpdateSettingsReqDto reqDto = om.readValue(params, UpdateSettingsReqDto.class);
        int noDisturbingTime = reqDto.getNoDisturbingTime();
        String repeatAlert = reqDto.getRepeatAlert();
        String reconnectInform = reqDto.getReconnectInform();
        setNoDisturbingTime(userId, "" + noDisturbingTime);
        userService.setRepeatAlert(userId, repeatAlert);
        userService.setReconnectInform(userId, reconnectInform);
        return NlostServer.SUCCESS;
    }

    public String updatePeriods(int userId, String params) throws Exception{
        User user = userRepo.findById(userId);
        silentPeriodRepo.deleteByUser_Id(userId);
        List<UpdatePeriodsReqDto> reqDtoList = om.readValue(params, new TypeReference<List<UpdatePeriodsReqDto>>() {});
        List<SilentPeriod> silentPeriodList = reqDtoList.stream().map(reqDto -> {
            SilentPeriod silentPeriod = new SilentPeriod();
            silentPeriod.setBeginTime(reqDto.getBeginTime());
            silentPeriod.setEndTime(reqDto.getEndTime());
            List dates = reqDto.getDates();
            StringBuffer strDates = new StringBuffer("");
            for (int i = 0; i < dates.size(); i++) {
                if (i == 0) {
                    strDates.append(dates.get(i));
                }else{
                    strDates.append("," + dates.get(i));
                }
            }
            silentPeriod.setDays(strDates.toString());
            silentPeriod.setOnOrOff(reqDto.getOnOrOff());
            silentPeriod.setUser(user);
            return silentPeriod;
        }).collect(Collectors.toList());
        silentPeriodRepo.save(silentPeriodList);
        return NlostServer.SUCCESS;
    }

    public String updateSilentAreas(int userId, String params) throws Exception{
        User user = userRepo.findById(userId);
        silentAreaRepo.deleteByUser_Id(userId);
        List<UpdateSilentAreaReqDto> reqDtoList = om.readValue(params, new TypeReference<List<UpdateSilentAreaReqDto>>() {});
        List<SilentArea> silentAreaList = reqDtoList.stream().map(reqDto -> {
            SilentArea silentArea = new SilentArea();
            silentArea.setName(reqDto.getName());
            silentArea.setCircleCenterLat(reqDto.getCircleCenterLat());
            silentArea.setCircleCenterLon(reqDto.getCircleCenterLon());
            silentArea.setRadius(reqDto.getRadius());
            silentArea.setOnOrOff(reqDto.getOnOrOff());
            silentArea.setUser(user);
            return silentArea;
        }).collect(Collectors.toList());
        silentAreaRepo.save(silentAreaList);
        return NlostServer.SUCCESS;
    }

    public String updateNoDisturbingTimeList(int userId, String params) throws Exception {
        List<Integer> list = om.readValue(params, new TypeReference<List<Integer>>() {});
        StringBuffer listStr = new StringBuffer("");
        for(int i=0; i<list.size(); i++) {
            listStr.append(list.get(i));
            listStr.append(",");
        }
        if (listStr.length() > 0) {
            listStr.deleteCharAt(listStr.length() - 1);    //减去最后一个','
        }

        User user = userRepo.findById(Integer.valueOf(userId));
        ItemProperty noDisturbingTimeList = itemPropertyRepo.findByKAndItem_Id("noDisturbingTimeList", userId);
        if (noDisturbingTimeList == null) {
            noDisturbingTimeList = new ItemProperty();
            noDisturbingTimeList.setItem(user);
            noDisturbingTimeList.setK("noDisturbingTimeList");
            noDisturbingTimeList.setV(listStr.toString());
        }else {
            noDisturbingTimeList.setV(listStr.toString());
        }
        itemPropertyRepo.save(noDisturbingTimeList);
        return NlostServer.SUCCESS;
    }

}
