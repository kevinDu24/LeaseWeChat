package com.tm.leasewechat.dto.result;

import lombok.Data;

/**
 * Created by LEO on 16/10/12.
 */
@Data
public class Result {
    private String resultMsg;
    private String resultCode;
    private Boolean isSuccess;
}
