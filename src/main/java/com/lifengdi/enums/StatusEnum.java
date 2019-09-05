package com.lifengdi.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 李锋镝
 * @date Create at 15:13 2019/4/2
 */
@Getter
public enum StatusEnum {

    JOB_TYPE_AUDITED("audited", "任务类型状态-审核通过")
    , COMMON_AUDITED("audited", "通用状态-审核通过")
    , JOB_EXECUTE_STATUS_WAIT("wait", "任务执行状态-待执行")
    , JOB_EXECUTE_STATUS_RUNNING("running", "任务执行状态-执行中")
    , JOB_EXECUTE_STATUS_SUCCESS("success", "任务执行状态-执行成功")
    , JOB_EXECUTE_STATUS_FAILED("failed", "任务执行状态-执行失败")
    ;

    private String status;

    private String description;

    StatusEnum(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public static List<Map<String, Object>> keyValue() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StatusEnum enumEntity : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", enumEntity.status);
            map.put("desc", enumEntity.description);
            list.add(map);
        }
        return list;
    }
}
