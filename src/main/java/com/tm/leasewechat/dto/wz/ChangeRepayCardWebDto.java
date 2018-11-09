package com.tm.leasewechat.dto.wz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 电子签约提交实体类(页面用)
 * Created by zcHu on 18/1/6.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeRepayCardWebDto {

    private String openId; // 微信openId
    private String applyNum; //申请编号
    private String phoneNum; //接收验证码手机号码
    private String terminalNum; //终端号码
    private String terminalName; //终端名称
    private String locationType; //LBS类型
    private String locationData; //LBS数据
    private String netWortType; //网络类型
    private String ip; //ip
    private String bank; //开户银行
    private String bankNum; //开户银行银联号
    private String bankCardNum; //用户卡号
    private String bankPhoneNum; //银行预留手机号
    private String contractNum; //合同编号
    private String frontImg; //银行卡正面照片
    private String behindImg; //银行卡反面照片

}
