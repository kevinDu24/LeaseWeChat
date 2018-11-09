package com.tm.leasewechat.dto.wz.result;

import lombok.Data;

/**
 * 银行列表实体类
 *
 * Created by huzongcheng on 18/1/9.
 */
@Data
public class BankListDto {
    private String bank; //银行名称
    private String bankNum; //银行联行号

    public BankListDto(Object[] objs) {
        this.bank = objs[0] == null ? "" : objs[0].toString();
        this.bankNum = objs[1] == null ? "" : objs[1].toString();
    }
}
