package com.gigaiot.nlostserver.dto.settingdto;

import lombok.Data;

import java.util.List;

/**
 * Created by cxm on 2017/10/10.
 */
@Data
public class AddPeriodResDto {
    private int id;
    private int beginTime;
    private int endTime;
    private List<Integer> dates;
    private byte onOrOff;
}
