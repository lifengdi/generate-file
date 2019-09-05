package com.lifengdi.model.pdf;

import lombok.Data;

/**
 * PDF印章配置
 * @author 李锋镝
 * @date Create at 11:16 2019/5/9
 */
@Data
public class PDFStamperConfig {

    /**
     * 是否生成印章
     */
    private Boolean generate = false;

    /**
     * 印章文件路径（PNG格式）
     */
    private String stamperUrl;

    /**
     * 生成印章的位置
     */
    private Location location;

    /**
     * 印章宽度
     */
    private Float fitWidth;

    /**
     * 印章高度
     */
    private Float fitHeight;

    /**
     * X偏移量
     */
    private Float xOffset;
    /**
     * Y偏移量
     */
    private Float yOffset;
}
