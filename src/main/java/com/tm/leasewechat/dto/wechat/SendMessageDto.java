package com.tm.leasewechat.dto.wechat;

import lombok.Data;

/**
 * Created by pengchao on 2017/4/13.
 */
@Data
public class SendMessageDto {
    private String touser;
    private String msgtype;
    private TextDto text;
    private ImageDto image;
    private String title;
    private String description;
    private String picurl;
    private String url;

}
