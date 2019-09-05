package com.lifengdi.model.query;

import lombok.Data;

/**
 * @author 李锋镝
 * @date Create at 15:04 2019/4/8
 */
@Data
public class JobLogQuery {

    private int page = 1;

    private int size = 10;

    private String jobId;

    private String jobTypeId;

    /**
     * 操作类型 see {@linkplain com.lifengdi.enums.OperateTypeEnum}
     */
    private String operateType;

    /**
     * 操作人的ID
     */
    private String operatorId;

    private String operatorPhone;

    private String orderBy;
}
