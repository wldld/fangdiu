package com.gigaiot.nlostserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigaiot.nlostserver.dto.corerequestdto.SingleIntParamReqDto;
import com.gigaiot.nlostserver.dto.unitdto.GetLocationDescriptionReqDto;
import com.gigaiot.nlostserver.dto.unitdto.GetLocationDescriptionResDto;
import com.gigaiot.nlostserver.dto.unitdto.GetUnitsResDto;
import com.gigaiot.nlostserver.entity.Unit;
import com.gigaiot.nlostserver.service.UnitService;
import com.gigaiot.nlostserver.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by cxm on 2017/9/26.
 */
@RestController
@RequestMapping("/nlost/ajax.html")
public class UnitController {
    @Autowired
    UnitService unitService;
    @Autowired
    ObjectMapper om;

    @RequestMapping(method = RequestMethod.POST, params = "svc=unit/saveUnit")
    public String saveUnit(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
//        Integer unitId = unitService.saveUnit(userId, params);
//        Map<String, Integer> response = new HashMap<>();
//        response.put("unitId", unitId);
//        return om.writeValueAsString(response);
        return unitService.saveUnit(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=unit/unbindUnit")
    public String unbindUnit(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return unitService.unbindUnit(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=unit/check")
    public String check(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        String userId = session.getUserId();
        return unitService.check(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=unit/updateUnitName")
    public String updateUnitName(HttpServletRequest req, String sid, String params) throws Exception{
        return unitService.updateUnitName(params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=unit/updateUnitPoint")
    public String updateUnitPoint(HttpServletRequest req, String sid, String params) throws Exception{
        return unitService.updateUnitPoint(params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=unit/updateUnitList")
    public String updateUnitList(HttpServletRequest req, String sid, String params) throws Exception{
        Session session = (Session) req.getAttribute("session");
        int userId = Integer.valueOf(session.getUserId());
        return unitService.updateUnitList(userId, params);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=unit/getLocationDescription")
    public String getLocationDescription(HttpServletRequest req, String sid, String params) throws Exception{
        GetLocationDescriptionReqDto reqDto = om.readValue(params, GetLocationDescriptionReqDto.class);
        String locationDescripition = unitService.getLocationDesciption(reqDto.getLat(), reqDto.getLon());
        GetLocationDescriptionResDto response = new GetLocationDescriptionResDto();
        response.setUnitId(reqDto.getUnitId());
        response.setLocationDescription(locationDescripition);
        return om.writeValueAsString(response);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=unit/getUnits")
    public String getUnits(HttpServletRequest req, String sid) throws Exception{
        Session session = (Session) req.getAttribute("session");
        List<Unit> itemList = unitService.getUnits(session.getUserId());
        List<GetUnitsResDto> resDtoList = itemList.stream().map(unit -> {
            GetUnitsResDto resDto = new GetUnitsResDto();
            resDto.setUnitId(unit.getId());
            resDto.setMac(unit.getMac());
            resDto.setName(unit.getName());
            resDto.setPhoto(unit.getPhotoPath());
            resDto.setLat(unit.getLat());
            resDto.setLon(unit.getLon());
            resDto.setLastActiveTime(unit.getLastActiveTime());
            resDto.setMacName(unit.getMacName());
            return resDto;
        }).collect(Collectors.toList());
        return om.writeValueAsString(resDtoList);
    }

    @RequestMapping(method = RequestMethod.POST, params = "svc=unit/getUnit")
    public String getUnit(HttpServletRequest req, String sid, String params) throws Exception{
        SingleIntParamReqDto reqDto = om.readValue(params, SingleIntParamReqDto.class);
        Session session = (Session) req.getAttribute("session");
        int unitId = reqDto.getData();
        return unitService.getUnit(Integer.valueOf(session.getUserId()), unitId);
    }


}
