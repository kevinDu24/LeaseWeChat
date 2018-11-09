package com.tm.leasewechat.consts;

/**
 * 微众交易类型
 * Created by zcHu on 2018/1/8.
 */
public enum WzSubmitType {


    /**
     * {@code 10031 一审提交}.
     */
    NEW("10031", "一审提交"),

    /**
     * {@code 10202 电子签约}.
     */
    SUBMIT("10202", "电子签约"),


    /**
     * {@code 10014 还款卡变更}.
     */
    CARDCHANGE("10014", "还款卡变更"),

    /**
     * {@code 10014 电子签约查询}.
     */
    SIGNQUERY("10090", "电子签约查询"),


    /**
     * {@code 10014 地址收货录入查询}.
     */
    ADDRESS_INPUT_QUERY("2018#001", "地址收货录入查询");


    private final String code;
    private final String value;

    public String code() {
        return this.code;
    }
    public String value() {
        return this.value;
    }

    WzSubmitType(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
