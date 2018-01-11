package com.gigaiot.nlostserver.dto.settingdto;

import com.gigaiot.nlostserver.entity.SilentArea;
import com.gigaiot.nlostserver.entity.SilentPeriod;
import com.gigaiot.nlostserver.entity.SilentWifi;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by cxm on 2017/11/16.
 */
@Data
public class GetSilentResDto {
//    private Map<String, List<SilentPeriod>> silentPeriod;
//    private Map<String, List<SilentArea>> silentArea;
//    private Map<String, List<SilentWifi>> silentWifi;
    private String silentPeriod;
    private String silentArea;
    private String silentWifi;
}
