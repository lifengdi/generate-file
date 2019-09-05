package com.lifengdi.enums;

import lombok.Getter;

/**
 * 操作类型
 * @author 李锋镝
 * @date Create at 18:47 2019/4/2
 */
@Getter
public enum OperateTypeEnum {

    ADD_JOB_TYPE("add_job_type", "新增任务类型")
    , ADD_JOB("add_job", "新增任务")
    , EXECUTE_JOB("execute_job", "执行任务")

    ;
    private String operateType;

    private String operateDesc;

    private String success;

    private String failed;

    private String begin;

    OperateTypeEnum(String operateType, String operateDesc) {
        this(operateType, operateDesc, "SUCCESS", "FAILED", "START_RUNNING");
    }

    OperateTypeEnum(String operateType, String operateDesc, String success, String failed, String begin) {
        this.operateType = operateType;
        this.operateDesc = operateDesc;
        this.success = success;
        this.failed = failed;
        this.begin = begin;
    }
}
