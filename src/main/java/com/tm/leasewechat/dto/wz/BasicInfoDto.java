package com.tm.leasewechat.dto.wz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/1/5.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicInfoDto {


    private String openId; // 微信openId

    private String name;//姓名

    private String phoneNum; //手机号

    private String idCard; //身份证号码
    
    private String bank; //开户银行
    
    private String bankNum; //开户银行银联号
    
    private String bankCardNum; //用户卡号

    private String ip; //IP地址

    private String locationType; //LBS提交类型

    private String locationData; //LBS数据

    private String netWortType; //网络状态

    private String fpName;//FP账号

    private String statement;//纳税声明

    private String monthlyIncome;//月收入水平

    private String uniqueMark;//app推送过来的唯一标识


}
