package com.lifengdi.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 下载文件DTO
 * @author 李锋镝
 * @date Create at 19:03 2019/6/20
 */
@Data
public class DownloadFileDTO {

    /* 需要下载的文件地址 */
    @NotEmpty
    private List<String> fileUrls;

    /**
     * 文件格式
     */
    private String fileFormat;
}
