package com.lifengdi.config;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PDF字体配置
 *
 * @author 李锋镝
 * @date Create at 14:14 2019/5/14
 */
@Component
@Slf4j
public class PDFFontConfig {

    public static final Map<String, BaseFont> FONT_MAP = new ConcurrentHashMap<>();

    public static final String SIM_SUN = "SimSun";
    public static final String SOURCE_HAN_SANS_CN = "SourceHanSansCN";

    static {
        try {
            FONT_MAP.put("SimSun", BaseFont.createFont(SystemConfig.FONT_PATH_SONG, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
            FONT_MAP.put("SourceHanSansCN", BaseFont.createFont(SystemConfig.SourceHanSansCN_Regular_TTF, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
        } catch (IOException | DocumentException e) {
            log.error("加载PDF字体异常", e);
        }
    }
}
