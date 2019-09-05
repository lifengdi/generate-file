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

    // 参数
    , PARAM_TYPE_NOT_FOUND("10100", "参数类型 %s 未找到")
    , REQUIRED_PARAM_CAN_NOT_BE_NULL("10101", "参数 %s 为必填参数,不能为空")
    , PARAM_FORMAT_ILLEGAL("10102", "参数 %s 类型或者格式不正确,正确类型为 %s")
    , PARAM_REPEAT("10103", "参数重复")

    // 文件
    , FILE_TYPE_ADD_SOURCE_ERROR("10200", "新增文件来源错误")
    , ADD_FILE_NO_ANALYSIS_MODEL_ERROR("10201", "新增文件未选择文件解析模型")
    , FILE_ANALYSIS_MODEL_NOT_SUPPORT("10202", "当前文件解析模型 %s 不支持解析")
    , ILLEGAL_FILE_UPLOAD("10203", "当前任务类型无需上传文件")
    , FILE_WITHOUT_URL("10204", "未检测到需要上传的文件")
    , FILE_FORMAT_ERROR("10205", "文件格式不正确")
    , NEED_UPLOAD_GENERATE_TEMPLATE_FILE("10206", "生成文件需要提交模板文件，请提交模板文件")
    , TEMPLATE_FILE_TOO_MORE("10207", "提交有重复的模板文件 %s")
    , FILE_ANALYSIS_EXCEPTION("10208", "文件解析异常")
    , CREATE_FILE_EXCEPTION("10209", "创建文件异常")

    // 任务类型
    , JOB_TYPE_NOT_FOUND_ID("10300", "修改任务类型时，任务类型ID不能为空")
    , JOB_TYPE_ID_CAN_NOT_BE_NULL("10301", "任务类型ID不能为空")
    , JOB_TYPE_NEED_UPLOAD_FILE_NO_PARAM_AND_TEMPLATE_FILE("10302", "需要上传文件的任务类型，请求参数和上传文件的解析模板二选一必传")

    // 任务
    , JOB_WITHOUT_JOB_TYPE("10400", "当前任务未选择任务类型")
    , ADD_JOB_NOT_FOUND_JOB_TYPE("10401", "未找到当前任务所选择的任务类型")
    , ADD_JOB_FOUND_JOB_TYPE_INVALID("10402", "当前任务所选择的任务类型无效")
    , JOB_ID_CAN_NOT_BE_NULL("10403", "任务ID不能为空")
    , JOB_TITLE_CAN_NOT_BE_NULL("10404", "任务标题不能为空")
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

