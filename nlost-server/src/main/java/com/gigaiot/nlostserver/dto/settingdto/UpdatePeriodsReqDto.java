package com.gigaiot.nlostserver.dto.settingdto;

import lombok.Data;

import java.util.List;

/**
 * Created by cxm on 2017/11/23.
 */
@Data
public class UpdatePeriodsReqDto {
    private long id;
    private int beginTime;
    private int endTime;
    private List<Integer> dates;
    byte onOrOff;
}
