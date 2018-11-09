package com.tm.leasewechat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by LEO on 16/9/27.
 * 暂未使用
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceApplyRecord {

    @Id
    @GeneratedValue
    private Long id;
    private String carBrand;
    private String carSeries;
    private String province;
    private String city;
    private String name;
    private String phoneNum;
    private String email;
    private String qq;
    private Integer applyType; // 申请类型,0为个人申请,1为企业申请
    private Integer carNum; // 购车数量
    private String purpose; // 购车用途
    private String companyName; // 企业名称
    private String fax; // 传真

}
