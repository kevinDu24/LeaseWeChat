package com.tm.leasewechat.dto.wz.result;

import com.tm.leasewechat.domain.ApplyInfo;
import lombok.Data;

/**
 * Created by pengchao on 2017/5/15.
 */
@Data
public class FinancePreApplyResultDto {

    private String userid;//经销商编号(用户名)

    private String applyId;//申请编号

    private String applyIdOfMaster;//主系统申请编号

    private String applyResult;//申请状态

    private String applyResultReason;//申请结果原因

    public FinancePreApplyResultDto(ApplyInfo applyInfo) {
        this.userid = applyInfo.getFpName();
        this.applyId = applyInfo.getUniqueMark();
        this.applyIdOfMaster = applyInfo.getApplyNum();
        this.applyResult = applyInfo.getStatus();
        this.applyResultReason = applyInfo.getReason();
    }

    public FinancePreApplyResultDto() {
    }
}
