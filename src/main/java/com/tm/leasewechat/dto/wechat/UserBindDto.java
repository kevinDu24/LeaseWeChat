package com.tm.leasewechat.dto.wechat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/9/23.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBindDto {
    private String name;
    private String cardId;
    private String wxCode;
    private String phoneNum;
    private String openId;
}
