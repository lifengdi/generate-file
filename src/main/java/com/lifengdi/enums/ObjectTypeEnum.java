package com.lifengdi.enums;

import lombok.Getter;

/**
 * @author 李锋镝
 * @date Create at 17:10 2019/4/2
 */
@Getter
public enum ObjectTypeEnum {
    SYSTEM(0)
    , JOB_TYPE(1)
    , JOB(2)
    ;

    private int type;

    ObjectTypeEnum(int type) {
        this.type = type;
    }
}
