package com.gigaiot.nlostserver.dto.eventdto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

import java.util.List;

/**
 * Created by zz on 2017/6/10.
 */
@Data
public class EventListDto {


    @JsonRawValue
    private List<String> events;

    private long tm; //时间
}
