package com.lifengdi.util;

import org.apache.commons.lang3.StringUtils;

/**
 * String工具类
 * @author 李锋镝
 * @date Create at 16:58 2019/5/13
 */
public class MyStringUtil {

    /**
     * 是否是http地址
     * @param path 要判断的字符串
     * @return boolean
     */
    public static boolean isHttpUrl(String path) {
        return StringUtils.startsWithIgnoreCase(path, "http");
    }

}
