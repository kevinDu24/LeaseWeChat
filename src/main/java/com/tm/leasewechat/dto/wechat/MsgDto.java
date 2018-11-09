package com.tm.leasewechat.dto.wechat;

import lombok.Data;

/**
 * Created by pengchao on 2017/8/30.
 */
@Data
public class MsgDto {
    private String errcode;
    private String errmsg;
    private String msgid;
    private String msg_id;
    private String msg_data_id;
    private String type;
}
