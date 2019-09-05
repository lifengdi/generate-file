package com.lifengdi.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author 李锋镝
 * @date Create at 14:39 2019/5/13
 */
@Data
public class PdfDTO {

    /**
     * 模板类型
     */
    @NotNull(message = "模板类型不能为空")
    private String type;

    /**
     * 数据
     */
    @NotNull(message = "填充数据不能为空")
    private Object data;

    /**
     * 生成文件的名字
     */
    private String targetName;
}
