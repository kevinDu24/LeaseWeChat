package com.tm.leasewechat.dto.customer;

import lombok.Data;

/**
 * Created by LEO on 16/10/11.
 */
@Data
public class SysUserDto {
    private String openId; // 微信openId
    private String cardId; // 客户身份证号
    private String name; // 客户姓名
    private String code; // 验证码
}
