package com.tm.leasewechat.dto.wz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电子签约查询实体类
 * Created by zcHu on 18/1/6.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignQueryDto {
    @JsonProperty
    private String BADDBH; //申请编号
    @JsonProperty
    private String TXN_ID; //交易类型


    @JsonIgnore
    public String getBASQBH() {
        return BADDBH;
    }
    @JsonIgnore
    public void setBASQBH(String BASQBH) {
        this.BADDBH = BASQBH;
    }
    @JsonIgnore
    public String getTXN_ID() {
        return TXN_ID;
    }
    @JsonIgnore
    public void setTXN_ID(String TXN_ID) {
        this.TXN_ID = TXN_ID;
    }

}
