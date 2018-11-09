package com.tm.leasewechat.utils;

/**
 * Created by pengchao on 2017/5/16.
 */
public class CommonUtils {

    public static String idType = "01";//证件提交类型 01 身份证;
    public static String signChannel = "03"; //签署渠道
    public static String errorInfo = "系统异常，请重试";

    public static boolean isNull(String param) {
        if (param == null)
            return true;
        else if (("").equals(param.trim()))
            return true;
        else
            return false;
    }

}
