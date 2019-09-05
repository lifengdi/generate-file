package com.lifengdi.job.entity;

import lombok.Data;

import java.time.Instant;

/**
 * 文件 t_file
 * 
 * @author goddess
 * 2019-09-05
 */
@Data
public class JobCenterFile {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String objId;

    /**
     * 
     */
    private Integer objType;

    /**
     * 文件CDN路径
     */
    private String fileCdn;

    /**
     * 
     */
    private String fileUrl;

    /**
     * 
     */
    private String fileType;

    /**
     * 文件解析模型
     */
    private String fileAnalysisModel;

    /**
     * 
     */
    private String status;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 
     */
    private Instant createdTime;

    /**
     * 文件临时访问地址
     */
    private String fileTempUrl;
}