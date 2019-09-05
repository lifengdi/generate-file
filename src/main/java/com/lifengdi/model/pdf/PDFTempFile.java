package com.lifengdi.model.pdf;

import lombok.Data;

/**
 * PDF模板配置
 * @author 李锋镝
 * @date Create at 11:10 2019/5/9
 */
@Data
public class PDFTempFile {
    /**
     * PDF模板文件类型
     */
    private String pdfType;

    /**
     * 模板文件名称
     */
    private String templateFileName;

    /**
     * 模板文件所在父级路径
     */
    private String templateFileParentPath;

    /**
     * 模板文件本地地址
     */
    private String templateFileLocalPath;
}
