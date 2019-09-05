package com.lifengdi.constant;

import com.lifengdi.exception.ApiException;
import com.lifengdi.response.ResponseResult;

/**
 * @author 李锋镝
 * @date Create at 15:17 2019/5/13
 */
public class ThreadLocalCache {
    /**
     * 最后一次请求信息
     */
    public static final ThreadLocal<ResponseResult> RESULT_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 请求错误信息
     */
    public static final ThreadLocal<ApiException> RESULT_ERROR_INFO_THREAD_LOCAL = new ThreadLocal<>();

}
