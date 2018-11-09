package com.tm.leasewechat.dto.sendall;

import lombok.Data;

/**
 * Created by pengchao on 2017/11/16.
 */
@Data
public class SendAllByTagDto {
    private FilterDto filter;
    private ImageDto image;
    private String msgtype;
    private TextDto text;
}
