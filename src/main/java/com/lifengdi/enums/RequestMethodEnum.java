package com.lifengdi.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 李锋镝
 * @date Create at 11:48 2019/4/12
 */
@Getter
public enum RequestMethodEnum {
    POST("POST")
    , GET("GET")
    ;

    private String requestMethod;

    RequestMethodEnum(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public static RequestMethodEnum get(String requestMethod) {
        for (RequestMethodEnum requestMethodEnum : values()) {
            if (requestMethodEnum.requestMethod.equals(requestMethod)) {
                return requestMethodEnum;
            }
        }
        return GET;
    }

    public static List<Map<String, Object>> keyValue() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RequestMethodEnum enumEntity : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", enumEntity.requestMethod);
            map.put("desc", enumEntity.requestMethod);
            list.add(map);
        }
        return list;
    }
}
