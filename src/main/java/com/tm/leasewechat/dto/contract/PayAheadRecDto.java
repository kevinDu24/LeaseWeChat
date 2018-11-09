package com.tm.leasewechat.dto.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/9/27.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayAheadRecDto {
    private String contractId; // 合同号码
    private String repayMoney; // 提前还款金额
    private String remainMoney; // 剩余本金
    private String monthInterest; // 当月利息
    private String commission; // 手续费
    private String breach; // 违约金
    private String deposit; // 保证金
    private String insurance; // 未退回保险
    private String date; // 提前还款申请日期
}
