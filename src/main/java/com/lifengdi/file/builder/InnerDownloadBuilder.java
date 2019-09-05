package com.lifengdi.file.builder;

import com.lifengdi.commons.utils.Either;
import com.lifengdi.config.QCloudCOSConfiguration;
import com.lifengdi.config.SystemConfig;
import com.lifengdi.exception.ApiException;
import com.lifengdi.exception.BaseException;
import com.lifengdi.model.dto.DownloadFileDTO;
import com.lifengdi.http.HttpUtil;
import com.lifengdi.util.GeneratedKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载文件到腾讯云
 * @author 李锋镝
 * @date Create at 19:59 2019/6/20
 */
@Component
@Slf4j
public class InnerDownloadBuilder implements IDownloadBuilder {

    @Resource
    private GeneratedKey generatedKey;

    @Resource
    private SystemConfig systemConfig;

    @Resource
    private QCloudCOSConfiguration qCloudCOSConfiguration;

    private static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(10);

    @Override
    public Either<ApiException, String> download(String fileUrl, String fileFormat) {

        // 文件后缀
        String suffix = fileUrl.substring(fileUrl.lastIndexOf("."), fileUrl.length());
        if (StringUtils.isNotBlank(suffix) && suffix.length() > 6) {
            suffix = "";
        }
        if (StringUtils.isNotBlank(fileFormat)) {
            suffix = fileFormat;
        }
        // 生成文件名
        String fileName = generatedKey.generatorKey() + suffix;
        String filePath = null;
        try {
            filePath = HttpUtil.downloadFile(fileUrl, fileName, systemConfig.getLocalTempPath());

            String qCloudKey = qCloudCOSConfiguration.putObject(fileName);
            log.info("InnerDownloadBuilder下载文件，qCloudKey:{}", qCloudKey);
            String qCloudUrl = qCloudCOSConfiguration.qCloudUrl(qCloudKey);
            log.info("qCloudKey:{}, qCloudUrl:{}", qCloudKey, qCloudUrl);
            return Either.right(qCloudUrl);
        } catch (Exception e) {
            log.error("下载文件失败", e);
        } finally {
            // 删除本地生成文件
            if (StringUtils.isNotBlank(filePath)) {
                File tempFile = new File(filePath);
                if (tempFile.exists() && tempFile.delete()) {
                    log.info("删除本地生成文件成功,filePath:{}", filePath);
                } else {
                    log.info("删除本地生成文件,文件不存在或者删除失败,filePath:{}", filePath);
                }
            }
        }
        return Either.left(BaseException.OPERATE_ERROR.build());
    }

    @Override
    public Either<ApiException, Map<String, String>> batchDownload(DownloadFileDTO dto) {


        List<String> urlList = dto.getFileUrls();
        Map<String, String> result = new HashMap<>();
        if (CollectionUtils.isEmpty(urlList)) {
            return Either.right(result);
        }

        CountDownLatch countDownLatch = new CountDownLatch(urlList.size());
        for (String url : urlList) {
            EXECUTORS.execute(() -> {
                Either<ApiException, String> either = download(url, dto.getFileFormat());
                if (either.isRight()) {
                    result.put(url, either.getRight());
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("线程中断异常", e);
            return Either.right(Collections.emptyMap());
        }

        return Either.right(result);
    }
}
