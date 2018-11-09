package com.tm.leasewechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by LEO on 16/10/20.
 */
@ConfigurationProperties(prefix = "wx")
@Data
public class WxProperties {
    private String contactUs;
    private String serverUrl;
    private String materialRequested;
    private String subscribeReply;
}
