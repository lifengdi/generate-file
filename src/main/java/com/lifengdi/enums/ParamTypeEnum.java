package com.lifengdi.enums;

import com.alibaba.fastjson.JSONArray;
import com.lifengdi.job.entity.RequestParam;
import com.lifengdi.util.DataTypeUtil;
import lombok.Getter;

import java.util.*;

/**
 * @author 李锋镝
 * @date Create at 18:10 2019/4/2
 */
@Getter
public enum ParamTypeEnum {

    STRING("string", "字符串")
    , NUMBER("number", "数字")
    , BOOLEAN("boolean", "布尔")
    , ARRAY_STRING("array_string", "数组<String>")
    , ARRAY_NUMBER("array_number", "数组<Number>")
    , ARRAY_BOOLEAN("array_boolean", "数组<Boolean>")
    , ARRAY_DATETIME("array_datetime", "数组<DateTime>")
    , DATETIME("datetime", "时间日期")
    , TIMESTAMP_MILL("timestamp_mill", "时间戳（毫秒）")
    , TIMESTAMP_SEC("timestamp_sec", "时间戳（秒）")
    ;
    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 参数类型的详细描述
     */
    private String typeDesc;

    ParamTypeEnum(String paramType, String typeDesc) {
        this.paramType = paramType;
        this.typeDesc = typeDesc;
    }

    public static List<Map<String, Object>> keyValue() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ParamTypeEnum enumEntity : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", enumEntity.paramType);
            map.put("desc", enumEntity.typeDesc);
            list.add(map);
        }
        return list;
    }

    /**
     * 判断参数类型
     * @param paramType paramType
     * @return boolean
     */
    public static boolean judgeParamType(String paramType) {
        for (ParamTypeEnum paramTypeEnum : values()) {
            if (paramTypeEnum.paramType.equals(paramType)) {
                return true;
            }
        }
        return false;
    }

    public static ParamTypeEnum get(String paramType) {
        for (ParamTypeEnum paramTypeEnum : values()) {
            if (paramTypeEnum.paramType.equals(paramType)) {
                return paramTypeEnum;
            }
        }
        return null;
    }

    /**
     * 判断参数格式
     * @param paramType paramType
     * @param param param
     * @param paramFormat 参数格式
     * @return boolean
     */
    public static boolean judgeParamFormat(String paramType, String param, String paramFormat) {

        ParamTypeEnum paramTypeEnum = get(paramType);

        if (Objects.isNull(paramTypeEnum)) {
            return false;
        }

        switch (paramTypeEnum){
            case NUMBER:
                return DataTypeUtil.isNumeric(param);
            case STRING:
                return true;
            case BOOLEAN:
                return DataTypeUtil.isBoolean(param);
            case DATETIME:
                return DataTypeUtil.isDateTime(param, paramFormat);
            case ARRAY_NUMBER:
                return DataTypeUtil.isArrayNumber(param);
            case ARRAY_STRING:
                return DataTypeUtil.isArrayString(param);
            case ARRAY_BOOLEAN:
                return DataTypeUtil.isArrayBoolean(param);
            case ARRAY_DATETIME:
                return DataTypeUtil.isArrayDateTime(param, paramFormat);
            case TIMESTAMP_SEC:
                return DataTypeUtil.isTimestampSec(param);
            case TIMESTAMP_MILL:
                return DataTypeUtil.isTimestampMill(param);
        }

        return false;
    }

    /**
     * 格式化参数
     * @param param
     * @return
     */
    public static Object formatParamValue(RequestParam param) {

        ParamTypeEnum paramTypeEnum = get(param.getParamType());

        if (Objects.isNull(paramTypeEnum)) {
            return null;
        }
        String paramValue = param.getParamValue();
        switch (paramTypeEnum){
            case NUMBER:
                return paramValue;
            case STRING:
                return paramValue;
            case BOOLEAN:
                return Boolean.parseBoolean(paramValue);
            case DATETIME:
                return paramValue;
            case ARRAY_NUMBER:
                return JSONArray.parseArray(paramValue);
            case ARRAY_STRING:
                return JSONArray.parseArray(paramValue);
            case ARRAY_BOOLEAN:
                return JSONArray.parseArray(paramValue);
            case ARRAY_DATETIME:
                return JSONArray.parseArray(paramValue);
            case TIMESTAMP_SEC:
                return paramValue;
            case TIMESTAMP_MILL:
                return paramValue;
        }

        return paramValue;
    }
}
