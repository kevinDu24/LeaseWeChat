package com.tm.leasewechat.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by wangbiao on 2016/12/10 0010.
 * 还款卡变更记录
 */

@Entity
@Data
public class RepaycardChangeRecord {
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

    public RepaycardChangeRecord(){

    }

    public RepaycardChangeRecord(String contractNum, String bankMsg, String bankCard, String frontMsg, String behindMsg){
        this.contractNum = contractNum;
        this.bankMsg = bankMsg;
        this.bankCard = bankCard;
        this.frontMsg = frontMsg;
        this.behindMsg = behindMsg;
    }


}
