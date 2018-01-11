package com.gigaiot.nlostserver.dto.eventdto;

import lombok.Data;

/**
 * Created by zz on 2017/6/10.
 */
@Data
public class EventDto {

    private Object d; //具体内容
    private String i; //unitId
    private String t; //类型   "n"   "u"  "d"  "m"
}
