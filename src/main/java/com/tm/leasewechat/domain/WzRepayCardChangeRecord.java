package com.tm.leasewechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.leasewechat.dto.wz.ChangeRepayCard;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by wangbiao on 2016/12/10 0010.
 * 微众还款卡变更表
 */

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WzRepayCardChangeRecord {

    @Id
    @GeneratedValue
    private Long id;
    private String cardId; // 客户身份证号
    private String name; // 客户姓名
    private String contractNum; //合同编号
    private String bankMsg; //变更后银行信息
    private String bankCard; //变更后还款卡卡号
    private String frontMsg; //银行卡正面
    private String behindMsg; //银行卡反面
    private String openId;//微信openId
    private String applyNum;//申请编号
    private String bankPhoneNum;//用户预留手机号
    private String bankNum;//变更后银联号
    private String message;//变更结果

    @CreatedDate
    private Date createTime;

    @CreatedBy
    private String createUser;

    @LastModifiedDate
    private Date updateTime;

    @LastModifiedBy
    private String updateUser;

    public WzRepayCardChangeRecord(){

    }

    public WzRepayCardChangeRecord(ChangeRepayCard changeRepayCard){
        this.bankCard = changeRepayCard.getBANK_CARD_NO();
        this.bankPhoneNum = changeRepayCard.getBANK_CARD_RESERVE_PHONE();
        this.bankNum = changeRepayCard.getBANK_CARD_BRNO();
        this.openId = changeRepayCard.getWX_OPENID();
        this.applyNum = changeRepayCard.getBASQBH();
        this.cardId = changeRepayCard.getID_NO();
        this.name = changeRepayCard.getNAME();
        this.bankMsg = changeRepayCard.getBANK_CARD_BRNAME();
        this.frontMsg = changeRepayCard.getKHZMFJ();
        this.behindMsg = changeRepayCard.getKHFMFJ();
    }


}
