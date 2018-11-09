package com.tm.leasewechat.dto.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/9/1.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonObject {
    private Object contractinfo;
    private Object contractrepayplaninfo;
    private Object repaymentInfoList;
    private Object earlyRepaymentInfo;
    private Object repaymentCardInfo;
    private Object repaymentBankList;
    private Object phone;
}
