package com.lifengdi.job.entity;

import java.time.Instant;
import lombok.Data;

/**
 * 任务类型 t_job_type
 * 
 * @author goddess
 * 2019-09-05
 */
@Data
public class JobType {
    /**
     * 自增ID
     */
    private Long id;

    /**
     * 任务ID
     */
    private String jobTypeId;

    /**
     * 任务类型描述
     */
    private String jobTypeDesc;

    /**
     * 是否生成文件 0-否，1-是
     */
    private Integer generateFile;

    /**
     * 是否上传文件 0-否 1-是
     */
    private Integer uploadFile;

    /**
     * 是否发送通知 0-否，1-是
     */
    private Integer sendNotification;

    /**
     * 通知类型 二进制
     */
    private Long notificationType;

    /**
     * 请求接口地址
     */
    private String requestUrl;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 是否开启允许操作人 0-否，1-是
     */
    private Integer enableAccess;

    /**
     * 接口响应体数据结构
     */
    private String responseDataEntity;

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
     * 状态
     */
    private String status;

    /**
     * 是否被删除 0-否 1-是
     */
    private Integer deleted;
}