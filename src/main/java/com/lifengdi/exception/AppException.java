package com.lifengdi.exception;

import com.lifengdi.exception.factory.ApiExceptionFactory;
import com.lifengdi.global.Global;
import lombok.Getter;

/**
 * @author 李锋镝
 * @date Create at 17:11 2018/7/5
 * @modified by
 */
@Getter
public enum AppException implements ApiExceptionFactory {

    TYPE_NOT_FOUND_EXCEPTION("100100", "类型没有找到")

    , TEMPLATE_FILENAME_NULL_EXCEPTION("100200", "配置的模板文件文件名为空")

    , GENERATE_FILE_FAILED("100300", "生成文件失败")
    ;

    @Override
    public String prefix() {
        return "BASE_";
    }

    private String code;

    private String message;

    AppException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiException build() {
        return apply(code, message);
    }

    public ApiException build(String msg) {
        return apply(code, message + Global.SPLIT_FLAG_COMMA + msg);
    }

    public ApiException builds(String ...msg) {
        return apply(code, String.format(message, msg));
    }

}

