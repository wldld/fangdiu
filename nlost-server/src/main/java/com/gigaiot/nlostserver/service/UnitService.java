package com.gigaiot.nlostserver.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigaiot.nlostserver.GeoCode;
import com.gigaiot.nlostserver.NlostClasses;
import com.gigaiot.nlostserver.NlostError;
import com.gigaiot.nlostserver.NlostServer;
import com.gigaiot.nlostserver.dto.corerequestdto.SingleIntParamReqDto;
import com.gigaiot.nlostserver.dto.corerequestdto.SingleParamRequestDto;
import com.gigaiot.nlostserver.dto.settingdto.UpdatePeriodsReqDto;
import com.gigaiot.nlostserver.dto.unitdto.*;
import com.gigaiot.nlostserver.entity.ItemRight;
import com.gigaiot.nlostserver.entity.Unit;
import com.gigaiot.nlostserver.entity.UpdateUnitListReqDto;
import com.gigaiot.nlostserver.entity.User;
import com.gigaiot.nlostserver.repository.ItemRightRepo;
import com.gigaiot.nlostserver.repository.UnitRepo;
import com.gigaiot.nlostserver.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxm on 2017/9/26.
 */
@Slf4j
@Service
public class UnitService {
    @Autowired
    ObjectMapper om;
    @Autowired
    UnitRepo unitRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ItemRightRepo itemRightRepo;
    @Autowired
    GeoCode geoCode;

    public String  saveUnit(String userId, String params) throws Exception{
        SaveUnitReqDto reqDto = om.readValue(params, SaveUnitReqDto.class);
        String macName = reqDto.getMacName();
        String mac = reqDto.getMac();
        String name = reqDto.getName();
        double lat = reqDto.getLat();
        double lon = reqDto.getLon();

        List<Unit> unitList = unitRepo.findByMacName(macName);
        Unit unit = null;
        if (unitList != null && unitList.size()>0) {
            unit = unitList.get(0);
            unit.setMacName(macName);
            unit.setMac(mac);
//            unit.setName(name);
            unit.setLat(lat);
            unit.setLon(lon);
            unit.setLastActiveTime(System.currentTimeMillis());
            unitRepo.save(unit);
        }else{
            unit = new Unit();
            unit.setMacName(macName);
            unit.setMac(mac);
            unit.setName(name);
            unit.setLat(lat);
            unit.setLon(lon);
            unit.setLastActiveTime(System.currentTimeMillis());
            unit.setCls(NlostClasses.UNIT);
            unitRepo.save(unit);
        }

        User user = userRepo.findById(Integer.valueOf(userId));

        ItemRight itemRight = itemRightRepo.findByUserIdAndItem_id(user.getId(), unit.getId());
        if (itemRight == null) {
            itemRight = new ItemRight();
            itemRight.setUserId(user.getId());
            itemRight.setItem(unit);
            itemRight.setValue(15);
            itemRightRepo.save(itemRight);
        }

        SaveUnitResDto resDto = new SaveUnitResDto();
        resDto.setUnitId(unit.getId());
        resDto.setMac(unit.getMac());
        resDto.setMacName(unit.getMacName());
        resDto.setName(unit.getName());
        resDto.setPhoto(unit.getPhotoPath());
        resDto.setLat(unit.getLat());
        resDto.setLon(unit.getLon());
        resDto.setLastActiveTime(unit.getLastActiveTime());

        return om.writeValueAsString(resDto);
    }

    public String unbindUnit(String userId, String params) throws Exception {
        SingleIntParamReqDto reqDto = om.readValue(params, SingleIntParamReqDto.class);
        int unitId = reqDto.getData();
        Unit unit = unitRepo.findById(unitId);
        String mac = unit.getMac();
        String macName = unit.getMacName();
        UnbindUnitResDto resDto = new UnbindUnitResDto();
        resDto.setMac(mac);
        resDto.setMacName(macName);
        itemRightRepo.deleteByUserIdAndItemId(Integer.valueOf(userId), unitId);
        return om.writeValueAsString(resDto);
    }

    public String check(String userId, String params) throws Exception{
        SingleParamRequestDto requestDto = om.readValue(params, SingleParamRequestDto.class);
        String macName = requestDto.getData();
        List<Unit> unitList = unitRepo.findByMacName(macName);  //测试的时候，数据库有多个重复数据，删数据又麻烦，所以用了list。
        Unit unit = null;
        if (unitList.size() > 0) {
            unit = unitList.get(0);
        }

        CheckUnitResDto resDto = new CheckUnitResDto();

        if (unit == null) {
            resDto.setCanBeConnected(true);
            resDto.setHasBeenBound(false);
            return om.writeValueAsString(resDto);
        }
        List<ItemRight> itemRightList = itemRightRepo.findByItem_id(unit.getId());

        if (itemRightList.size() == 0) {
            resDto.setCanBeConnected(true);
            resDto.setHasBeenBound(false);
        }else{
            boolean flag = false;
            for (ItemRight itemRight : itemRightList) {
                if (itemRight.getUserId() == Integer.valueOf(userId)) {
                    flag = true;
                }
            }
            if (flag) {
                resDto.setCanBeConnected(true);
                resDto.setHasBeenBound(true);
            }else {
                resDto.setCanBeConnected(false);
                resDto.setHasBeenBound(true);
            }
        }
        return om.writeValueAsString(resDto);
    }

    public String updateUnitName(String params) throws Exception {
        UpdateUnitNameReqDto reqDto = om.readValue(params, UpdateUnitNameReqDto.class);
        String name = reqDto.getName();
        int unitId = reqDto.getUnitId();

        Unit unit = unitRepo.findById(unitId);
        unit.setName(name);

        unitRepo.save(unit);

        return NlostServer.SUCCESS;
    }

    public String updateUnitPoint(String params) throws Exception{
        UpdateUnitPointReqDto reqDto = om.readValue(params, UpdateUnitPointReqDto.class);
        int unitId = reqDto.getUnitId();
        double lat = reqDto.getLat();
        double lon = reqDto.getLon();
        log.debug("lat:" + lat);
        log.debug("lon:" + lon);
        log.debug("unitId:" + unitId);
        Unit unit = unitRepo.findById(unitId);
        log.debug("unit:" + unit);
        unit.setLat(lat);
        unit.setLon(lon);
        unit.setLastActiveTime(System.currentTimeMillis());
        unitRepo.save(unit);
        Map<String, Long> response = new HashMap<>();
        response.put("lastActiveTime", unit.getLastActiveTime());
        return om.writeValueAsString(response);
    }

    public String getLocationDesciption(double lat, double lon) throws Exception{
        return geoCode.decode(lat, lon);
    }

    public List<Unit> getUnits(String userId) {
//        List<Item> unitList = itemRightRepo.findItemByUserIdAndItem_cls(Integer.valueOf(userId), 2);
//        List<ItemRight> itemRightList = itemRightRepo.findByUserIdAndItem_cls(Integer.valueOf(userId), 2);
//        List<Unit> unitList = itemRightList.stream().map(itemRight -> {
//            Unit unit = (Unit) itemRight.getItem();
//            return unit;
//        }).collect(Collectors.toList());
//        return unitList;
        List<Unit> unitList = unitRepo.findByUserIdAndItemCls(Integer.valueOf(userId), NlostClasses.UNIT);
        return  unitList;
    }

    public String getUnit(int userId, int unitId) throws Exception{
        ItemRight itemRight = itemRightRepo.findByUserIdAndItem_id(userId, unitId);
        if (itemRight == null) {
            return new NlostError(NlostError.ACCESS_DENIED).toString();
        }
        Unit unit = unitRepo.findById(unitId);
        GetUnitsResDto response = new GetUnitsResDto();
        response.setUnitId(unit.getId());
        response.setName(unit.getName());
        response.setMac(unit.getMac());
        response.setPhoto(unit.getPhotoPath());
        response.setLastActiveTime(unit.getLastActiveTime());
        response.setLat(unit.getLat());
        response.setLon(unit.getLon());
        response.setMacName(unit.getMacName());
        return om.writeValueAsString(response);
    }

    public String updateUnitList(int userId, String params) throws Exception{
        List<UpdateUnitListReqDto> reqDtoList = om.readValue(params, new TypeReference<List<UpdateUnitListReqDto>>() {});
        for (UpdateUnitListReqDto reqDto : reqDtoList) {
            int unitId = reqDto.getUnitId();
            ItemRight itemRight = itemRightRepo.findByUserIdAndItem_id(userId, unitId);
            if (itemRight != null) {
                unitRepo.updateById(unitId, reqDto.getName(), reqDto.getLat(), reqDto.getLon(), reqDto.getLastActiveTime());
            }
        }
        return NlostServer.SUCCESS;
    }

}
