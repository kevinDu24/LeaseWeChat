package com.tm.leasewechat.dto.wechat;

import lombok.Data;

/**
 * Created by pengchao on 2017/11/14.
 */
@Data
public class TemplateMessageDto {
    private KeyNoteDto first;
    private KeyNoteDto keyword1;
    private KeyNoteDto keyword2;
    private KeyNoteDto remark;
}
