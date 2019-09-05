package com.lifengdi.job.entity;

import lombok.Data;

import java.time.Instant;

/**
 * 任务类型 t_job
 * 
 * @author goddess
 * 2019-09-05
 */
@Data
public class Job {
    /**
     * 自增ID
     */
    private Long id;

    /**
     * 任务ID
     */
    private String jobId;

    /**
     * job_type_id
     */
    private String jobTypeId;

    /**
     * 任务标题
     */
    private String jobTitle;

    /**
     * 任务类型描述
     */
    private String jobTypeDesc;

    /**
     * 是否发送通知 0-否，1-是
     */
    private Integer sendNotification;

    /**
     * 创建时间
     */
    private Instant createdTime;

    /**
     * 创建人ID
     */
    private String createdUserId;

    /**
     * 创建人
     */
    private String createdUserName;

    /**
     * 创建人手机号
     */
    private String createdUserPhone;

    /**
     * 最后一次修改时间
     */
    private Instant updatedTime;

    /**
     * 最后一次修改人ID
     */
    private String updatedUserId;

    /**
     * 最后一次修改人
     */
    private String updatedUserName;

    /**
     * 最后一次修改人phone
     */
    private String updatedUserPhone;

    /**
     * 任务执行状态
     */
    private String executeStatus;

    /**
     * 状态
     */
    private String status;

    /**
     * 是否被删除 0-否 1-是
     */
    private Integer deleted;
}