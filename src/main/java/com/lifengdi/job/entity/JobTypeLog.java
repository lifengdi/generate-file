package com.lifengdi.job.entity;

import java.time.Instant;
import lombok.Data;

/**
 * 任务类型操作日志 t_job_type_log
 * 
 * @author goddess
 * 2019-09-05
 */
@Data
public class JobTypeLog {
    /**
     * 自增ID
     */
    private Long id;

    /**
     * 任务类型ID
     */
    private String jobTypeId;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 操作前内容
     */
    private String oldContent;

    /**
     * 操作后内容
     */
    private String newContent;

    /**
     * 操作描述
     */
    private String operateDesc;

    /**
     * 
     */
    private Instant createdTime;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人姓名
     */
    private String operatorName;
}