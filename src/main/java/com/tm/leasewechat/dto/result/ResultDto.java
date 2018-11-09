package com.tm.leasewechat.dto.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by LEO on 16/10/12.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultDto {
    private String message;
    private Object result;
}
