package com.lifengdi.config;

import com.lifengdi.model.pdf.PDFType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * PDF配置
 * @author 李锋镝
 * @date Create at 11:10 2019/5/9
 */
@Data
@Component
@ConfigurationProperties("pdf")
public class PDFConfig {

    private Map<String, PDFType> pdfType = new HashMap<>();
}
