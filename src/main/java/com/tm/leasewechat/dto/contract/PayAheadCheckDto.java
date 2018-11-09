package com.tm.leasewechat.dto.contract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by ZCHu on 17/3/2.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayAheadCheckDto {
    private String yqzt; // 逾期状态
    private String sczt; // 收车状态
    private String sszt; // 诉讼状态
    private String basqbh; // 合同号码
}
