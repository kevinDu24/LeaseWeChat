package com.tm.leasewechat.dto.wz;

import lombok.Data;

/**
 * Created by pengchao on 2017/5/15.
 */
@Data
public class FinancePreApplyResultWxDto {

    private String userId;//经销商编号(用户名)

    private String uniqueMark ;//微众单子唯一标识

    private String applyNum;//申请编号

    private String name;//申请人姓名

    private String idCard;//申请人身份证号

    private String phoneNum;//申请人手机号

    private String bankNum;//申请人银行卡卡号

    private String bank;//申请人银行名称

    public FinancePreApplyResultWxDto(BasicInfoDto basicInfoDto) {
        this.userId = basicInfoDto.getFpName();
        this.applyNum = "" + (int)(Math.random()*90000000+70000000);
        this.name = basicInfoDto.getName();
        this.idCard = basicInfoDto.getIdCard().toUpperCase();
        this.phoneNum  = basicInfoDto.getPhoneNum();
        this.bankNum = basicInfoDto.getBankCardNum();
        this.bank = basicInfoDto.getBank();
    }
}
