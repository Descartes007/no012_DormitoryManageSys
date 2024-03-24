package com.hzvtc.myproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 熊新欣
 * @date 2020-12-08
 */
@ConfigurationProperties(prefix = "constant")
public class Constant {
    public final static String HEADER_TOKEN = "_ut";
    public final static String DEFAULT_PASSWORD = "123456";
    public static String UPLOAD_PATH;


    @Value("${constant.upload-path}")
    public void setUploadPath(String uploadPath) {
        UPLOAD_PATH = uploadPath;
    }
}
