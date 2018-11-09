package com.tm.leasewechat.dto.wz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tm.leasewechat.config.WzProperties;
import com.tm.leasewechat.utils.CommonUtils;
import com.tm.leasewechat.utils.Utils;
import com.tm.leasewechat.consts.GlobalConsts;
import com.tm.leasewechat.consts.WzSubmitType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 电子签约提交实体类
 * Created by zcHu on 18/1/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeRepayCard {
    @JsonProperty
    private String BASQBH; //申请编号
    @JsonProperty
    private String TXN_ID; //交易类型
    @JsonProperty
    private String ID_TYPE; //证件提交类型 01
    @JsonProperty
    private String WX_OPENID; //微信OpenId
    @JsonProperty
    private String APP_TYPE; //应用提交类型 W
    @JsonProperty
    private String APP_ID; //应用ID
    @JsonProperty
    private String WX_UNION_ID; //微信唯一id 无
    @JsonProperty
    private String BANK_CARD_USAGE; //卡用途 4
    @JsonProperty
    private String BANK_CARD_NO; //卡号
    @JsonProperty
    private String BANK_CARD_BRNO; //银联号
    @JsonProperty
    private String BANK_CARD_BRNAME; //开户行名
    @JsonProperty
    private String BANK_CARD_RESERVE_PHONE; //用户预留手机号
    @JsonProperty
    private String CLICK_SMS_TIME; //点击“获取验证码”时间
    @JsonProperty
    private String SYS_SEND_SMS_TIME; //系统发送验证码时间
    @JsonProperty
    private String CHECK_SMS_MOBILE; //接收验证码手机号码
    @JsonProperty
    private String CHECK_SMS_SUC_TIME; //验短通过时间
    @JsonProperty
    private String APPLY_TIME; //提交申请时间
    @JsonProperty
    private String TERMINAL_NUM; //终端号码 null
    @JsonProperty
    private String TERMINAL_NAME; //终端名称 null
    @JsonProperty
    private String LOCATION_TYPE; //LBS类型
    @JsonProperty
    private String LOCATION_DATA; //LBS数据
    @JsonProperty
    private String NETWORT_TYPE; //网络类型
    @JsonProperty
    private List<ContractBaseDto> CONTRACT_BASE; //合同协议
    @JsonProperty
    private String IP; // ip


    @JsonProperty
    private String NAME; //姓名
    @JsonProperty
    private String ID_NO; //姓名
    @JsonProperty
    private String OS_TYPE = "NONE";//操作系统
    @JsonProperty
    private String MOBILE_BRANDS;//手机品牌
    @JsonProperty
    private String IOS_IDFA;//idfa
    @JsonProperty
    private String ANDROID_IMEI;//imei
    @JsonProperty
    private String MAC_ADDR;//mac地址

    @JsonProperty
    private String KHZMFJ;//银行卡正面附件

    @JsonProperty
    private String KHFMFJ;//银行卡反面面附件









    public ChangeRepayCard(ChangeRepayCardWebDto changeRepayCardWebDto, WzProperties wzProperties) {
        Date nowDate = new Date();
        String date2 = Utils.getStrDate(nowDate,Utils.yyyymmddhhmmss);
        this.BASQBH = changeRepayCardWebDto.getApplyNum();
        this.TXN_ID = WzSubmitType.CARDCHANGE.code();
        this.ID_TYPE = CommonUtils.idType;
        this.WX_OPENID = changeRepayCardWebDto.getOpenId();
        this.APP_TYPE = wzProperties.getAppType();
        this.APP_ID = GlobalConsts.WeChat.APPID.value();
        this.BANK_CARD_USAGE = wzProperties.getBankCardUsage();
        this.BANK_CARD_NO = changeRepayCardWebDto.getBankCardNum();
        this.BANK_CARD_BRNO = changeRepayCardWebDto.getBankNum();
        this.BANK_CARD_BRNAME = changeRepayCardWebDto.getBank();
        this.BANK_CARD_RESERVE_PHONE = changeRepayCardWebDto.getPhoneNum();
        this.CLICK_SMS_TIME = date2;
        this.SYS_SEND_SMS_TIME = date2;
        this.CHECK_SMS_MOBILE = changeRepayCardWebDto.getPhoneNum();
        this.CHECK_SMS_SUC_TIME = date2;
        this.APPLY_TIME = date2;
        this.LOCATION_TYPE = changeRepayCardWebDto.getLocationType();
        this.LOCATION_DATA = changeRepayCardWebDto.getLocationData();
        this.NETWORT_TYPE = changeRepayCardWebDto.getNetWortType();
        this.IP = changeRepayCardWebDto.getIp();
        this.CONTRACT_BASE = buildContract(wzProperties, date2);
        this.KHZMFJ = changeRepayCardWebDto.getFrontImg();
        this.KHFMFJ = changeRepayCardWebDto.getBehindImg();
    }

    private List<ContractBaseDto> buildContract(WzProperties wzProperties, String time) {
        List<ContractBaseDto> contractList = new ArrayList();
        ContractBaseDto dto = new ContractBaseDto();
        dto.setCONTRACT_NAME(wzProperties.getDk().getName());
        dto.setCONTRACT_VER(wzProperties.getDk().getVersion());
        dto.setCHECK_TIME(time);
        contractList.add(dto);
        return contractList;
    }
    @JsonIgnore
    public String getNAME() {
        return NAME;
    }
    @JsonIgnore
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
    @JsonIgnore
    public String getID_NO() {
        return ID_NO;
    }
    @JsonIgnore
    public void setID_NO(String ID_NO) {
        this.ID_NO = ID_NO;
    }
    @JsonIgnore
    public String getOS_TYPE() {
        return OS_TYPE;
    }
    @JsonIgnore
    public void setOS_TYPE(String OS_TYPE) {
        this.OS_TYPE = OS_TYPE;
    }
    @JsonIgnore
    public String getMOBILE_BRANDS() {
        return MOBILE_BRANDS;
    }
    @JsonIgnore
    public void setMOBILE_BRANDS(String MOBILE_BRANDS) {
        this.MOBILE_BRANDS = MOBILE_BRANDS;
    }
    @JsonIgnore
    public String getIOS_IDFA() {
        return IOS_IDFA;
    }
    @JsonIgnore
    public void setIOS_IDFA(String IOS_IDFA) {
        this.IOS_IDFA = IOS_IDFA;
    }
    @JsonIgnore
    public String getANDROID_IMEI() {
        return ANDROID_IMEI;
    }
    @JsonIgnore
    public void setANDROID_IMEI(String ANDROID_IMEI) {
        this.ANDROID_IMEI = ANDROID_IMEI;
    }
    @JsonIgnore
    public String getMAC_ADDR() {
        return MAC_ADDR;
    }
    @JsonIgnore
    public void setMAC_ADDR(String MAC_ADDR) {
        this.MAC_ADDR = MAC_ADDR;
    }

    @JsonIgnore
    public String getBASQBH() {
        return BASQBH;
    }
    @JsonIgnore
    public void setBASQBH(String BASQBH) {
        this.BASQBH = BASQBH;
    }
    @JsonIgnore
    public String getTXN_ID() {
        return TXN_ID;
    }
    @JsonIgnore
    public void setTXN_ID(String TXN_ID) {
        this.TXN_ID = TXN_ID;
    }
    @JsonIgnore
    public String getWX_OPENID() {
        return WX_OPENID;
    }
    @JsonIgnore
    public void setWX_OPENID(String WX_OPENID) {
        this.WX_OPENID = WX_OPENID;
    }
    @JsonIgnore
    public String getAPP_TYPE() {
        return APP_TYPE;
    }
    @JsonIgnore
    public void setAPP_TYPE(String APP_TYPE) {
        this.APP_TYPE = APP_TYPE;
    }
    @JsonIgnore
    public String getAPP_ID() {
        return APP_ID;
    }
    @JsonIgnore
    public void setAPP_ID(String APP_ID) {
        this.APP_ID = APP_ID;
    }
    @JsonIgnore
    public String getWX_UNION_ID() {
        return WX_UNION_ID;
    }
    @JsonIgnore
    public void setWX_UNION_ID(String WX_UNION_ID) {
        this.WX_UNION_ID = WX_UNION_ID;
    }
    @JsonIgnore
    public String getCLICK_SMS_TIME() {
        return CLICK_SMS_TIME;
    }
    @JsonIgnore
    public void setCLICK_SMS_TIME(String CLICK_SMS_TIME) {
        this.CLICK_SMS_TIME = CLICK_SMS_TIME;
    }
    @JsonIgnore
    public String getSYS_SEND_SMS_TIME() {
        return SYS_SEND_SMS_TIME;
    }
    @JsonIgnore
    public void setSYS_SEND_SMS_TIME(String SYS_SEND_SMS_TIME) {
        this.SYS_SEND_SMS_TIME = SYS_SEND_SMS_TIME;
    }
    @JsonIgnore
    public String getCHECK_SMS_MOBILE() {
        return CHECK_SMS_MOBILE;
    }
    @JsonIgnore
    public void setCHECK_SMS_MOBILE(String CHECK_SMS_MOBILE) {
        this.CHECK_SMS_MOBILE = CHECK_SMS_MOBILE;
    }
    @JsonIgnore
    public String getCHECK_SMS_SUC_TIME() {
        return CHECK_SMS_SUC_TIME;
    }
    @JsonIgnore
    public void setCHECK_SMS_SUC_TIME(String CHECK_SMS_SUC_TIME) {
        this.CHECK_SMS_SUC_TIME = CHECK_SMS_SUC_TIME;
    }
    @JsonIgnore
    public String getAPPLY_TIME() {
        return APPLY_TIME;
    }
    @JsonIgnore
    public void setAPPLY_TIME(String APPLY_TIME) {
        this.APPLY_TIME = APPLY_TIME;
    }
    @JsonIgnore
    public String getTERMINAL_NUM() {
        return TERMINAL_NUM;
    }
    @JsonIgnore
    public void setTERMINAL_NUM(String TERMINAL_NUM) {
        this.TERMINAL_NUM = TERMINAL_NUM;
    }
    @JsonIgnore
    public String getTERMINAL_NAME() {
        return TERMINAL_NAME;
    }
    @JsonIgnore
    public void setTERMINAL_NAME(String TERMINAL_NAME) {
        this.TERMINAL_NAME = TERMINAL_NAME;
    }
    @JsonIgnore
    public String getLOCATION_TYPE() {
        return LOCATION_TYPE;
    }
    @JsonIgnore
    public void setLOCATION_TYPE(String LOCATION_TYPE) {
        this.LOCATION_TYPE = LOCATION_TYPE;
    }
    @JsonIgnore
    public String getLOCATION_DATA() {
        return LOCATION_DATA;
    }
    @JsonIgnore
    public void setLOCATION_DATA(String LOCATION_DATA) {
        this.LOCATION_DATA = LOCATION_DATA;
    }
    @JsonIgnore
    public String getNETWORT_TYPE() {
        return NETWORT_TYPE;
    }
    @JsonIgnore
    public void setNETWORT_TYPE(String NETWORT_TYPE) {
        this.NETWORT_TYPE = NETWORT_TYPE;
    }
    @JsonIgnore
    public List<ContractBaseDto> getCONTRACT_BASE() {
        return CONTRACT_BASE;
    }
    @JsonIgnore
    public void setCONTRACT_BASE(List<ContractBaseDto> CONTRACT_BASE) {
        this.CONTRACT_BASE = CONTRACT_BASE;
    }
    @JsonIgnore
    public String getIP() {
        return IP;
    }
    @JsonIgnore
    public void setIP(String IP) {
        this.IP = IP;
    }
    @JsonIgnore
    public String getBANK_CARD_USAGE() {
        return BANK_CARD_USAGE;
    }
    @JsonIgnore
    public void setBANK_CARD_USAGE(String BANK_CARD_USAGE) {
        this.BANK_CARD_USAGE = BANK_CARD_USAGE;
    }
    @JsonIgnore
    public String getBANK_CARD_NO() {
        return BANK_CARD_NO;
    }
    @JsonIgnore
    public void setBANK_CARD_NO(String BANK_CARD_NO) {
        this.BANK_CARD_NO = BANK_CARD_NO;
    }
    @JsonIgnore
    public String getBANK_CARD_BRNO() {
        return BANK_CARD_BRNO;
    }
    @JsonIgnore
    public void setBANK_CARD_BRNO(String BANK_CARD_BRNO) {
        this.BANK_CARD_BRNO = BANK_CARD_BRNO;
    }
    @JsonIgnore
    public String getBANK_CARD_BRNAME() {
        return BANK_CARD_BRNAME;
    }
    @JsonIgnore
    public void setBANK_CARD_BRNAME(String BANK_CARD_BRNAME) {
        this.BANK_CARD_BRNAME = BANK_CARD_BRNAME;
    }
    @JsonIgnore
    public String getBANK_CARD_RESERVE_PHONE() {
        return BANK_CARD_RESERVE_PHONE;
    }
    @JsonIgnore
    public void setBANK_CARD_RESERVE_PHONE(String BANK_CARD_RESERVE_PHONE) {
        this.BANK_CARD_RESERVE_PHONE = BANK_CARD_RESERVE_PHONE;
    }
    @JsonIgnore
    public String getID_TYPE() {
        return ID_TYPE;
    }
    @JsonIgnore
    public void setID_TYPE(String ID_TYPE) {
        this.ID_TYPE = ID_TYPE;
    }

    @JsonIgnore
    public String getKHZMFJ() {
        return KHZMFJ;
    }
    @JsonIgnore
    public void setKHZMFJ(String KHZMFJ) {
        this.KHZMFJ = KHZMFJ;
    }
    @JsonIgnore
    public String getKHFMFJ() {
        return KHFMFJ;
    }
    @JsonIgnore
    public void setKHFMFJ(String KHFMFJ) {
        this.KHFMFJ = KHFMFJ;
    }
}
