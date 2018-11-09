package com.tm.leasewechat.dto.wechat;

import lombok.Data;
import net.sf.json.JSONObject;

/**
 * Created by pengchao on 2017/4/13.
 */
@Data
public class SendTemplateMessageDto {
    private String touser;
    private String template_id;
    private String url;
    private JSONObject data;
}
