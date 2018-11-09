package com.tm.leasewechat.dto.contract;

import com.tm.leasewechat.dto.result.Result;
import lombok.Data;

import java.util.List;

/**
 * Created by ZCHu on 17/3/2.
 */
@Data
public class PayAheadCheckResultDto {
    private Result res;
    private List<PayAheadCheckDto> earlyRepayReport;
}
