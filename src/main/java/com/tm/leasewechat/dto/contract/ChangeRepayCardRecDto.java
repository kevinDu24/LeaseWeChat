package com.tm.leasewechat.dto.contract;

import lombok.Data;

/**
 * Created by LEO on 16/9/27.
 */
@Data
public class ChangeRepayCardRecDto {
    private String bankMsg;
    private String bankCard;
    private String frontMsg;
    private String behindMsg;
    private String contractId;
}
