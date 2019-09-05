package com.lifengdi.job.entity;

import java.time.Instant;
import lombok.Data;

/**
 * 任务操作日志 t_job_log
 * 
 * @author goddess
 * 2019-09-05
 */
@Data
public class JobLog {
    /**
     * id
     */
    private Long id;

    /**
     * 任务ID
     */
    private String jobId;

    /**
     * 
     */
    private String jobTypeId;

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 操作结果
     */
    private String operateResult;

    /**
     * 操作人的ID
     */
    private String operatorId;

    /**
     * 
     */
    private String operatorName;

    /**
     * 
     */
    private String operatorPhone;

    /**
     * 
     */
    private Instant createdTime;
}