package com.lifengdi.file.builder;

import com.lifengdi.commons.utils.Either;
import com.lifengdi.exception.ApiException;
import com.lifengdi.model.DownloadFileDTO;

import java.util.Map;

/**
 * @author 李锋镝
 * @date Create at 19:15 2019/6/20
 */
public interface IDownloadBuilder {

    /**
     * 下载文件
     * @param fileUrl 文件地址
     * @param fileFormat 文件格式
     * @return Either
     */
    Either<ApiException, String> download(String fileUrl, String fileFormat);

    /**
     * 批量下载文件
     * @param dto 文件地址
     * @return Either
     */
    Either<ApiException, Map<String, String>> batchDownload(DownloadFileDTO dto);
}
