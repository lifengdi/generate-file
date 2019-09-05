package com.lifengdi.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 李锋镝
 * @date Create at 14:35 2019/4/3
 */
@Getter
public enum FileTypeEnum {

    UPLOAD_FILE_TEMP("upload_file_temp", "上传文件模板", ObjectTypeEnum.JOB_TYPE)
    , UPLOAD_DATA_FILE("upload_data_file", "上传的数据文件", ObjectTypeEnum.JOB)
    , DOWNLOAD_FILE_TEMP("download_file_temp", "下载文件模板", ObjectTypeEnum.JOB_TYPE)
    , DOWNLOAD_DATA_FILE("download_data_file", "下载的数据文件", ObjectTypeEnum.SYSTEM)
    ;

    private String fileType;

    private String typeDesc;

    /**
     * 文件新增来源 {@linkplain ObjectTypeEnum}
     */
    private ObjectTypeEnum fileSource;

    FileTypeEnum(String fileType, String typeDesc, ObjectTypeEnum fileSource) {
        this.fileType = fileType;
        this.typeDesc = typeDesc;
        this.fileSource = fileSource;
    }

    /**
     * 判断文件来源
     * @param fileSource {@linkplain ObjectTypeEnum}
     * @return boolean
     */
    public static boolean judgeFileSource(ObjectTypeEnum fileSource) {
        for (FileTypeEnum fileTypeEnum : values()) {
            if (fileTypeEnum.fileSource.equals(fileSource)) {
                return true;
            }
        }
        return false;
    }

    public static List<Map<String, Object>> keyValue() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FileTypeEnum fileTypeEnum : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", fileTypeEnum.fileType);
            map.put("desc", fileTypeEnum.typeDesc);
            map.put("fileSource", fileTypeEnum.fileSource);
            list.add(map);
        }
        return list;
    }
}
