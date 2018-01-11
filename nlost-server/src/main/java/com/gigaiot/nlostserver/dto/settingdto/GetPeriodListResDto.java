package com.gigaiot.nlostserver.dto.settingdto;

import lombok.Data;

import java.util.List;

/**
 * Created by cxm on 2017/10/11.
 */
@Data
public class GetPeriodListResDto {
    private int id;
    private int beginTime;
    private int endTime;
    private List<Integer> dates;
    private byte onOrOff;
}
