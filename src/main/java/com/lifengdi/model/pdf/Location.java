package com.lifengdi.model.pdf;

import lombok.Data;

/**
 * 页面位置坐标
 * @author 李锋镝
 * @date Create at 11:35 2019/5/9
 */
@Data
public class Location {

    /**
     * X坐标
     */
    private Float x;

    /**
     * Y坐标
     */
    private Float y;

    /**
     * 横向间隔
     */
    private Float xInterval;

    /**
     * 纵向间隔
     */
    private Float yInterval;

    /**
     * 指定页 -1:全部，0:最后一页，1:首页
     */
    private Integer page;

    /**
     * 在指定文字上盖章
     */
    private String word;

    /**
     * 水印是否在内容上边
     */
    private Boolean overContent;
}
