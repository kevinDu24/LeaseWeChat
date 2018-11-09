package com.tm.leasewechat.domain;

import com.tm.leasewechat.dto.wechat.UserBindDto;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by pengchao on 2018/1/3.
 * 微众用户绑定表
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wzCustomer")
public class WzSysUser {
    @Id
    @GeneratedValue
    private Long id;
    private String openId; // 微信openId
    private String name; // 客户姓名
    private String code; // 验证码
    private String phoneNum; // 手机号
    private String unionId;// 微信unionId
    @LastModifiedDate
    private Date updateTime;//更新时间

    @CreatedDate
    private Date createTime ; //绑定时间

    public WzSysUser(){}

    public WzSysUser(String openId, UserBindDto userBindDto, String code){
        this.openId = openId;
        this.name = userBindDto.getName();
        this.code = code;
        this.phoneNum = userBindDto.getPhoneNum();
    }
}