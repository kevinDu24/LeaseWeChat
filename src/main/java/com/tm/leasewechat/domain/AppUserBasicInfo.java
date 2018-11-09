package com.tm.leasewechat.domain;

import com.tm.leasewechat.dto.wz.AppUserBasicInfoDto;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by pengchao on 2017/5/15.
 * 暂未使用
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class AppUserBasicInfo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String fpName;//经销商编号(用户名)

    private String uniqueMark ;//唯一标识

    private String name;//申请人姓名

    private String idCard;//申请人身份证号

    private String otherPhoneNum;//申请人手机号

    private String bankCardNum;//申请人银行卡卡号

    private String bank;//申请人银行名称

    private String phoneNum;//银行预留手机号(需带入一审页面)
    
    private String homeNumber; //住宅电话
    
    private String vicePhoneNumber;//副手机号;

    private String origin;//来源[2: APP在线助力融]

    @LastModifiedDate
    private Date updateTime; //更新时间

    @CreatedDate
    private Date createTime; //提交时间

    public AppUserBasicInfo(AppUserBasicInfoDto appUserBasicInfoDto) {
        this.fpName = appUserBasicInfoDto.getFpName();
        this.uniqueMark = appUserBasicInfoDto.getUniqueMark();
        this.name = appUserBasicInfoDto.getName();
        this.idCard = appUserBasicInfoDto.getIdCard();
        this.otherPhoneNum = appUserBasicInfoDto.getOtherPhoneNum();
        this.bankCardNum = appUserBasicInfoDto.getBankCardNum();
        this.bank = appUserBasicInfoDto.getBank();
        this.phoneNum = appUserBasicInfoDto.getPhoneNum();
        this.homeNumber = appUserBasicInfoDto.getHomeNumber();
        this.vicePhoneNumber = appUserBasicInfoDto.getVicePhoneNumber();
        this.origin = appUserBasicInfoDto.getOrigin();
    }

    public AppUserBasicInfo() {
    }
}

