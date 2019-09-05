package com.lifengdi.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用枚举类
 *
 * @author 李锋镝
 * @date Create at 20:53 2018/12/14
 */
@Getter
public enum GlobalEnum {

    NO(0, "否")
    , YES(1, "是");

    private int value;

    private String name;

    GlobalEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static List<Map<String, Object>> keyValue() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (GlobalEnum enumEntity : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", enumEntity.value);
            map.put("desc", enumEntity.name);
            list.add(map);
        }
        return list;
    }
}
