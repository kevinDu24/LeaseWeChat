package com.tm.leasewechat.config;

import com.tm.leasewechat.dto.wechat.TemplateDto;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by pengchao on 2018/1/16.
 */
@ConfigurationProperties(prefix = "templateMessage")
@Data
public class TemplateProperties {

    private TemplateDto pushOrder;//太盟宝手机号推送模板

    private TemplateDto applyResult;//主系统申请状态推送模板

    private TemplateDto contractSign;//电子签约推送模板

    private TemplateDto repayCardChange;//还款卡变更推送模板
}
