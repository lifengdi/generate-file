package com.lifengdi.file.builder;

import com.alibaba.fastjson.JSON;
import com.lifengdi.exception.ApiException;
import com.lifengdi.commons.utils.Either;
import com.lifengdi.config.QCloudCOSConfiguration;
import com.lifengdi.config.SystemConfig;
import com.lifengdi.exception.AppException;
import com.lifengdi.model.pdf.PDFTempFile;
import com.lifengdi.model.pdf.PDFType;
import com.lifengdi.file.pdf.HtmlToPDF;
import com.lifengdi.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Objects;

/**
 * HTML转PDF
 * @author 李锋镝
 * @date Create at 20:34 2019/5/9
 */
@Component
@Slf4j
public class HtmlToPDFBuilder extends AbsPDFBuilder {

    @Resource
    private HtmlToPDF htmlToPDF;


    @Resource
    private SystemConfig systemConfig;

    @Resource
    private QCloudCOSConfiguration qCloudCOSConfiguration;

    @Override
    public Either<ApiException, String> builder(String buildFileName, String type, Object data) {

        log.info("生成PDF文件, buildFileName:{}, type:{}, data:{}", buildFileName, type, JSON.toJSONString(data));
        PDFType pdfType = (PDFType) getType(type);
        if (Objects.isNull(pdfType)) {
            log.info("根据type匹配不到模板文件，type:{}", type);
            return Either.left(AppException.TYPE_NOT_FOUND_EXCEPTION.build());
        }
        PDFTempFile pdfTempFile = pdfType.getPdfTempFile();
        String localTempFile = null;
        try {
            localTempFile = loadTempFileToLocal(pdfTempFile);
            if (StringUtils.isNotBlank(localTempFile)) {
                // 生成PDF
                buildFileName = htmlToPDF.createPDF(htmlToPDF.matchDataToHtml(data, pdfTempFile), buildFileName);
                log.info("生成PDF文件,文件名:{}", buildFileName);
                if (StringUtils.isNotBlank(buildFileName)) {
                    // 渲染PDF文件
                    log.info("渲染PDF文件,文件名:{},PdfStamperConfig:{},PdfWatermarkConfig:{}", buildFileName, pdfType.getPdfStamperConfig(), pdfType.getPdfWatermarkConfig());
                    htmlToPDF.renderLayer(buildFileName, buildFileName, pdfType.getPdfStamperConfig(), pdfType.getPdfWatermarkConfig());
                    // 上传腾讯云
                    String cdnKey = qCloudCOSConfiguration.putObject(buildFileName);
                    String qCloudUrl = qCloudCOSConfiguration.qCloudUrl(cdnKey);
                    log.info("上传腾讯云,CDNKey:{},qCloudUrl:{}", cdnKey, qCloudUrl);
                    return Either.right(qCloudUrl);
                }
            }
        } catch (Exception e) {
            log.error("根据HTML模板生成PDF文件出现异常", e);
        } finally {
            // 删除临时模板文件
            if (MyStringUtil.isHttpUrl(pdfTempFile.getTemplateFileParentPath())
                    && StringUtils.isNotBlank(localTempFile)) {
                File tempFile = new File(systemConfig.getLocalTempPath() + localTempFile);
                if (tempFile.exists() && tempFile.delete()) {
                    log.info("删除临时模板文件成功,localTempFile:{}", localTempFile);
                } else {
                    log.info("删除临时模板文件,文件不存在或者删除失败,localTempFile:{}", localTempFile);
                }
            }
            // 删除本地生成文件
            if (StringUtils.isNotBlank(buildFileName)) {
                File tempFile = new File(htmlToPDF.getTargetFileTempPath(buildFileName));
                if (tempFile.exists() && tempFile.delete()) {
                    log.info("删除本地生成文件成功,buildFileName:{}", buildFileName);
                } else {
                    log.info("删除本地生成文件,文件不存在或者删除失败,buildFileName:{}", buildFileName);
                }
            }
        }

        return Either.left(AppException.GENERATE_FILE_FAILED.build());
    }

}
