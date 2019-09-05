package com.lifengdi.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.xhtmlrenderer.css.newmatch.PageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口响应体中data的结构
 * @author 李锋镝
 * @date Create at 15:38 2019/4/2
 */
@Getter
public enum ResponseDataEntityEnum {
    PAGE_HELPER_PAGE_INFO("com.github.pagehelper.PageInfo", PageInfo.class)
    , SPRING_PAGE("org.springframework.data.domain.Page", Page.class)
    , JSON_OBJECT("com.alibaba.fastjson.JSONObject", JSONObject.class)
    , JSON_ARRAY("com.alibaba.fastjson.JSONArray", JSONArray.class)
    ;
    private String classPath;

    private Class clazz;

    ResponseDataEntityEnum(String classPath, Class clazz) {
        this.classPath = classPath;
        this.clazz = clazz;
    }

    public static ResponseDataEntityEnum get(String classPath) {
        for (ResponseDataEntityEnum entityEnum : values()) {
            if (entityEnum.classPath.equals(classPath)) {
                return entityEnum;
            }
        }
        return SPRING_PAGE;
    }

    public static List<Map<String, Object>> keyValue() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResponseDataEntityEnum enumEntity : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", enumEntity.classPath);
            map.put("desc", enumEntity.clazz);
            list.add(map);
        }
        return list;
    }
}
