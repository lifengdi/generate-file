package com.lifengdi.job.entity;

import java.time.Instant;
import lombok.Data;

/**
 * 请求参数 t_request_param
 * 
 * @author goddess
 * 2019-09-05
 */
@Data
public class RequestParam {
    /**
     * 自增ID
     */
    private Long id;

    /**
     * 任务ID
     */
    private String objId;

    /**
     * 
     */
    private Integer objType;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数title
     */
    private String paramTitle;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 参数默认值
     */
    private String paramValue;

    /**
     * 参数格式（主要针对datetime类型的）
     */
    private String paramFormat;

    /**
     * 是否必填 0-否，1-是
     */
    private Integer required;

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