package com.tm.leasewechat.domain;

import com.tm.leasewechat.dto.wechat.UserBindDto;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by LEO on 16/9/26.
 * 系统用户表
 */
@Entity
@Data
@Table(name = "customer")
public class SysUser {
    @Id
    @GeneratedValue
    private Long id;
    private String openId; // 微信openId
    private String cardId; // 客户身份证号
    private String name; // 客户姓名
    private String code; // 验证码
    private String phoneNum; // 手机号
    private String unionId;// 微信unionId

    public SysUser(){}
    
    public SysUser(String openId, UserBindDto userBindDto, String code){
        this.openId = openId;
        this.cardId = userBindDto.getCardId();
        this.name = userBindDto.getName();
        this.code = code;
        this.phoneNum = userBindDto.getPhoneNum();
    }
}
