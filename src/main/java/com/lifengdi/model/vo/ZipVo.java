package com.lifengdi.model.vo;

import lombok.Data;

/**
 * 压缩包公共结构体
 */
@Data
public class ZipVo {

    /**
     * bizCode
     */
    private String bizCode;

    /**
     * 被压缩文件url
     */
    private String url;

}
