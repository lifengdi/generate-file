package com.lifengdi.model;

import com.lifengdi.enums.ParamTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 李锋镝
 * @date Create at 19:25 2019/4/8
 */
@NoArgsConstructor
@Data
public class ParamBO {

    /**
     * 参数名
     */
    private String paramKey;
    /**
     * 参数类型
     */
    private ParamTypeEnum paramType;
    /**
     * 参数格式
     */
    private String paramFormat;
    /**
     * 参数值
     */
    private String paramValue;

    public ParamBO(String paramKey) {
        this.paramKey = paramKey;
    }
}
