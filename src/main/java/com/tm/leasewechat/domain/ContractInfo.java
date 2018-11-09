package com.tm.leasewechat.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by pengchao on 2018/1/15.
 * 电子签约信息表
 */

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "wzContractInfo")
public class ContractInfo {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid")
    private String id;

    private String applyNum; //申请编号

    private String totalInvestment; //融资总额

    private String financeTerm; //融资期限

    private String monthPay; //月供

    private String financeAmount; //车辆融资额

    private String purchaseTax; //购置税

    private String retrofittingFee; //加装费

    private String extendedWarranty; //延保

    private String gps; //GPS硬件

    private String compulsoryInsurance; //交强险保费

    private String commercialInsurance; //商业险保费

    private String vehicleTax; //车船税

    private String unexpected; //意外保障

    private String xfws; //先锋卫士

    private String otherFee; //其他费用

    @CreatedDate
    private Date createTime; //创建时间

    private String name; //借款人姓名

    private String idCardNum; //借款人身份证号

    private String carName; //车辆名称

    private String grantBank; //贷款发放银行 微众银行

    private String creditFinanceAmount; //信用融资额

    private String rates; //贷款年利率

}
