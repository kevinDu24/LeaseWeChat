package com.tm.leasewechat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LEO on 16/9/1.
 */
public class Utils {
    public static Boolean isCardId(String str){
        return str.matches("(\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");
    }

    public static Boolean isNumber(String str){
        return str.matches("[0-9]+");
    }

    public static SimpleDateFormat yyyymmdd =new SimpleDateFormat("YYYYMMdd");

    public static SimpleDateFormat yyyymmddhhmmss =new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 获取文件后缀名(包含.)
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String buildText(String text){
        StringBuffer buffer = new StringBuffer();
        String array[] = text.split(";");
        for (String s : array) {
            buffer.append(s.trim()).append("\n");
        }
        return buffer.toString();
    }

    /**
     * 将日期转换为字符串显示
     * @param time
     * @param simpleDateFormat
     * @return
     */
    public static String getStrDate(Date time, SimpleDateFormat simpleDateFormat){
        if (time == null) {
            return null;
        }
        try{
            return simpleDateFormat.format(time);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
