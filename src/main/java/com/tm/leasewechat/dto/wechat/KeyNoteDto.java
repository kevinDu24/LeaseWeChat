package com.tm.leasewechat.dto.wechat;

import lombok.Data;

/**
 * Created by pengchao on 2017/8/30.
 */
@Data
public class KeyNoteDto {
    private String value;
    private String color;

    public KeyNoteDto(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public KeyNoteDto() {
    }

    public KeyNoteDto(String value) {
        this.value = value;
        this.color = "#173177";
    }
}
