package com.tm.leasewechat.dto.wechat;

import lombok.Data;

/**
 * Created by LEO on 16/9/23.
 */
@Data
public class VerificationDto {
    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;
}
