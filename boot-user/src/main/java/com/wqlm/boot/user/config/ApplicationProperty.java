package com.wqlm.boot.user.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 将 properties 中自定义的属性，都统一映射到该类
 */
@Getter
@Component
public class ApplicationProperty {

    @Value("${noAuthUrls}")
    private String noAuthUrls;

    @Value("${cache.expireTime}")
    private int cacheExpireTime;

    @Value("${sessionTtl}")
    private int sessionTtl;

    public String[] getNoAuthUrls() {
        return noAuthUrls.split(",");
    }
}
