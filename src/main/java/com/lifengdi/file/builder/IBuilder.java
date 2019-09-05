package com.lifengdi.file.builder;

import com.lifengdi.commons.utils.Either;
import com.lifengdi.exception.ApiException;

/**
 *
 * @author 李锋镝
 * @date Create at 20:25 2019/5/9
 */
public interface IBuilder {

    /**
     * 生成文件
     * @param buildFileName 生成文件名称
     * @param type 类型
     * @param data 数据
     * @return 生成的文件的地址
     */
    Either<ApiException, String> builder(String buildFileName, String type, Object data);
}
