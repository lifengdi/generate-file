package com.lifengdi.model.query;

import com.lifengdi.enums.GlobalEnum;
import lombok.Data;

/**
 * @author 李锋镝
 * @date Create at 10:35 2019/4/8
 */
@Data
public class JobQuery {

    private int page = 1;

    private int size = 10;

    /**
     * 创建人ID
     */
    private String createdUserId;

    /**
     * 创建人手机号
     */
    private String createdUserPhone;

    /**
     * 状态
     */
    private String status;

    /**
     * 任务执行状态
     */
    private String executeStatus;

    /**
     * 是否被删除 0-否 1-是
     */
    private Integer deleted = GlobalEnum.NO.getValue();

    /**
     * 任务标题
     */
    private String jobTitle;

    /**
     * 所属任务类型
     */
    private String jobTypeId;

    private String orderBy;
}
