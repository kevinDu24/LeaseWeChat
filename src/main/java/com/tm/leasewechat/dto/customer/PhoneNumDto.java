package com.tm.leasewechat.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/9/27.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneNumDto {
    private Object phone;
}
