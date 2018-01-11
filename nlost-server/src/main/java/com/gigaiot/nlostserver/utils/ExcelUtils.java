package com.gigaiot.nlostserver.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxm on 2017/8/15.
 */
public class ExcelUtils {
    public static void generateExcel(String fileName, String sheetName, String[][] titles, List<?> data) throws Exception{
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd hh:mm:ss");

        //写入标题
        HSSFRow row = sheet.createRow(0);
        for(int i=0; i<titles.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(titles[i][1]);
        }

        //写入正文数据
        if (data != null && data.size() > 0) {
            Class objClass = data.get(0).getClass();
            for(int k=0; k<data.size(); k++){
                Object obj = data.get(k);
                HSSFRow row2 = sheet.createRow(k + 1);
                for(int j=0; j<titles.length; j++) {
                    try {
                        String fieldName = titles[j][0];
                        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Method method = objClass.getMethod(getMethodName);
                        String value = String.valueOf(method.invoke(obj));
                        if ("receivedTime".equals(fieldName)) {
                            row2.createCell(j).setCellValue(dateFormat.format(Long.valueOf(value)));
                        }else if("engineRunTime".equals(fieldName)){
                            row2.createCell(j).setCellValue(getTime(Long.valueOf(value)));
                        }else{
                            row2.createCell(j).setCellValue(Double.valueOf(value));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        FileOutputStream out = new FileOutputStream(fileName);
        wb.write(out);

    }


    public static void main(String[] args) throws Exception {
//        String[][] str = {{"unitId","Generator L1-N voltage"},{"no", "Gen L2"},{"alarmName", "Gen L2"}};
//        List<AlertItem> data = new ArrayList<AlertItem>();
//        for(int i=0; i<3; i++) {
//            AlertItem alertItem = new AlertItem();
//            alertItem.setUnitId(1);
//            alertItem.setNo((short)1);
//            alertItem.setAlarmName("no 1");
//            data.add(alertItem);
//        }
//        generateExcel("temp/fileName3", "sheetName", str, data);
    }

    public static String getTime(long timestamp) {
        long date = timestamp/(1000*60*60*24);
        timestamp = timestamp % (1000 * 60 * 60 * 24);
        long hour = timestamp / (1000 * 60 * 60);
        timestamp = timestamp % (1000 * 60 * 60);
        long min = timestamp / (1000 * 60);
        timestamp = timestamp % (1000 * 60);
        long sec = timestamp / 1000;
        String time = date + " dates  " + hour + ":" + min + ":" + sec;
        return time;
    }

}
