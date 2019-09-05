package com.lifengdi.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 李锋镝
 * @date Create at 10:51 2019/5/9
 */
@Getter
@Configuration
public class SystemConfig {
    /**
     * 文件本地缓存目录
     */
    @Value("${file.localTempPath}")
    private String localTempPath;

    /**
     * 项目中自带字体的目录
     */
    public static final String FONT_PATH_SONG = "font/simsun.ttf";

    public static final String SourceHanSansCN_Regular_TTF = "font/SourceHanSansCN-Regular.ttf";

    /**
     * 项目中的缓存目录
     */
    @Value("${file.appTempFolder:file/}")
    private String appTemplateFolder;
}
