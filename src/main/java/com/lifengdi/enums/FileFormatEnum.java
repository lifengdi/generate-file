package com.lifengdi.enums;

import lombok.Getter;

/**
 * 文件格式枚举
 */
@Getter
public enum FileFormatEnum {

    NO_CREATE_FILE(0,"不生成文件"),
    CREATE_EXCEL_FILE(1,"生成Excel文件"),
    CREATE_PDF_FILE(2,"生成PDF文件");

    /**
     * 参数key
     */
    private int key;

    /**
     * 参数value
     */
    private String value;

    FileFormatEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

}
