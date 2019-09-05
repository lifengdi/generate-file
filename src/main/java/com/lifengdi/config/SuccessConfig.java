package com.lifengdi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李锋镝
 * @date Create at 16:36 2019/5/6
 */
@Data
@Component
@ConfigurationProperties("success")
public class SuccessConfig {

    private Map<String, String> successCode = new HashMap<>();
}
