package com.tm.leasewechat.dto.wz.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by pengchao on 2018/2/7.
 */

@Data
@JsonIgnoreProperties(ignoreUnknown =true)
public class ChangeRepayCardResultDto {

    private String userName; //用户姓名

    private String bankCardNum; //银行卡号

    private String message; //变更结果

    private String applyNum; //申请编号

    private String uniqueMark; //唯一标识


}
