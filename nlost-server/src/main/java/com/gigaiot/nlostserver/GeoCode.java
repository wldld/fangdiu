package com.gigaiot.nlostserver;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by cxm on 2017/8/23.
 */
@Slf4j
@Component
public class GeoCode {
    @Value("${geoCodeHost}")
    private String geoCodeHost;
    @Value("${geoCodePort}")
    private String geoCodePort;

    public  String decode(double lat, double lon){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(geoCodeHost);
        url.append(":");
        url.append(geoCodePort);
        url.append("/gis_geoquery?");
        url.append("lat=" + lat);
        url.append("&");
        url.append("lon=" + lon);
        log.info(url.toString());


        HttpGet httpGet = new HttpGet(url.toString());
        CloseableHttpResponse response = null;
        String content = "";
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        try {
            response = httpClient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String str = null;
            while((str=reader.readLine())!=null){
                content += str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return content;
    }

    public static void main(String[] args) throws Exception {
//        String str = decode(22.2, 113.3);
//        System.out.println(str);
    }

//    public static void dateFormat() {
//        String dateStr = "1502940673000";
//
//    }

}
