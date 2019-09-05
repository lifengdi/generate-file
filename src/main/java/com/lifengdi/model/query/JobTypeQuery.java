package com.lifengdi.model.query;

import com.lifengdi.enums.GlobalEnum;
import lombok.Data;

/**
 * @author 李锋镝
 * @date Create at 16:52 2019/4/3
 */
@Data
public class JobTypeQuery {

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
     * 请求方式
     */
    private String requestMethod;

    /**
     * 是否被删除 0-否 1-是
     */
    private Integer deleted = GlobalEnum.NO.getValue();

    private String jobTypeDesc;

    private String orderBy;

}
