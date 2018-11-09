package com.tm.leasewechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by wisely on 2015/10/23.
 */
@ConfigurationProperties(prefix = "file")
@Data
public class FileUploadProperties {
    private String filePath;
    private String requestFilePath;
}
