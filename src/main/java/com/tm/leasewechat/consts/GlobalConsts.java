package com.tm.leasewechat.consts;

/**
 * Created by LEO on 16/9/1.
 */
public enum  GlobalConsts {
    SIGN("12940581fbbf2df3b9739fe34a3344b8"), TIMESTAMP("1429604397531");

    private final String value;

    GlobalConsts(String value) {
        this.value = value;
    }

    public String value(){
        return this.value;
    }

    public enum WeChat {
//        APPID("wx6c7845b6d077314e"), APPSECRET("2127c086391e43f91252213828b9cb2c"), TOKEN("dys"); //ys
        APPID("wxc6b7a0fe2ee928fb"), APPSECRET("9bf66f0a0d063404d7e279a971067ac7"), TOKEN("cqc");   //cqc
//        APPID("wx743de4a4fa762293"), APPSECRET("54164220134bbd5b9d872ccd544eea1a"), TOKEN("tmwechat");
        private final String value;

        WeChat(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
}
