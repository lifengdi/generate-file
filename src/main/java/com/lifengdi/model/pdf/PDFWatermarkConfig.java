package com.lifengdi.model.pdf;

import lombok.Data;

/**
 * PDF文件水印配置
 * @author 李锋镝
 * @date Create at 11:17 2019/5/9
 */
@Data
public class PDFWatermarkConfig {

    /**
     * 是否生成水印
     */
    private Boolean generate;

    /**
     * 水印文字
     */
    private String watermarkWord;

    /**
     * 水印文字颜色 red,green,blue
     */
    private String watermarkColor = "128,128,128";

    /**
     * 水印透明度
     */
    private Float fillOpacity = 0.1F;

    /**
     * 水印文字大小
     */
    private Integer fontSize = 38;

    /**
     * 水印文字字体
     */
    private String fontFamily;

    /**
     * 生成水印的位置，不指定则默认整页
     */
    private Location location;

}
