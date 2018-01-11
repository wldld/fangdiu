package com.gigaiot.nlostserver.controller;

import com.gigaiot.nlostserver.NlostError;
import com.gigaiot.nlostserver.NlostServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by cxm on 2017/11/20.
 */
@Slf4j
@Controller
@RequestMapping("/nlost/map")
public class MapController {

    @RequestMapping(value = "/getMap", method = RequestMethod.GET)
    public void getMap(HttpServletRequest req, HttpServletResponse res,int mt, int x, int y, int z) throws Exception{
        String path = System.getProperty("user.dir") + "/map/";
        File f= new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        String img = f.getAbsolutePath()+ "/" + mt+"-"+x+"-"+y+"-"+z+".png";
        File imgFile = new File(img);

        if (imgFile.exists()) {
            log.info("map file already exists.");
            long expires = 180L * 24L * 60L * 60L * 1000L; //过期时间180天
//            long expires = 1 * 60 * 1000; //1 分钟
//            long expires = 10 *1000; // 10 秒
            long current = System.currentTimeMillis();
            long lastModified = imgFile.lastModified();
            long result =current - lastModified;
            if (result > expires) {
                log.info("map file expires");
                imgFile.delete();
                log.info("map file deleted");
            }
        }

        if(!imgFile.exists()){
            boolean flag = false;
            int i = 0;
            do {
                flag = getMapFromGoogle(mt, x, y, z, path);
                i++;
            } while (i <= 3 && false==flag);
            if (!flag) {
                throw new Exception("请求外部地图失败");
            }
        }

        FileInputStream is = new FileInputStream(imgFile);
        res.setHeader("Cache-Control","max-age=604800");
        OutputStream os = res.getOutputStream();
        byte[] buffer = new byte[1024 * 20];
        int len;
        while((len=is.read(buffer))!=-1){
            os.write(buffer, 0, len);
        }
        os.flush();
        if (is != null) {
            is.close();
        }
        if (os != null) {
            os.close();
        }
    }

    public boolean getMapFromGoogle(int mt, int x, int y, int z, String outPath) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringBuffer url = new StringBuffer();
        url.append("http://")
                .append("mt").append(mt)
                .append(".google.cn/vt/lyrs=m&")
                .append("&x=").append(x)
                .append("&y=").append(y)
                .append("&z=").append(z);
        log.info(url.toString());

        HttpGet httpGet = new HttpGet(url.toString());
        CloseableHttpResponse response = null;
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        try {
            response = httpClient.execute(httpGet);
            InputStream is = response.getEntity().getContent();
            File file = new File(outPath+"/"+mt+"-"+x+"-"+y+"-"+z+".png");
            FileOutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 20];
            int len;
            while((len=is.read(buffer))!=-1){
                os.write(buffer, 0, len);
            }
            os.flush();
            if (os != null) {
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
