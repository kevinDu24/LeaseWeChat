package com.tm.leasewechat.dto.wz;

import lombok.Data;

/**
 * Created by pengchao on 2018/1/5.
 */
@Data
public class LocationInfoDto {


    private String locationType; //LBS提交类型

    private String locationData; //LBS数据

    private String netWortType; //网络状态

}
