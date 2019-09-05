package com.lifengdi.file.builder;

import com.lifengdi.config.PDFConfig;
import com.lifengdi.config.SystemConfig;
import com.lifengdi.exception.AppException;
import com.lifengdi.http.HttpUtil;
import com.lifengdi.model.IType;
import com.lifengdi.model.pdf.PDFTempFile;
import com.lifengdi.model.pdf.PDFType;
import com.lifengdi.util.GeneratedKey;
import com.lifengdi.util.MyStringUtil;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 生成PDF抽象类
 * @author 李锋镝
 * @date Create at 20:28 2019/5/9
 */
public abstract class AbsPDFBuilder implements IBuilder{

    @Resource
    private PDFConfig pdfConfig;

    @Resource
    private SystemConfig systemConfig;

    @Resource
    private GeneratedKey generatedKey;

    /**
     * 获取要生成PDF的类型
     * @param type 类型
     * @return IType
     */
    public IType getType(String type) {
        Map<String, PDFType> pdfType = pdfConfig.getPdfType();
        if (pdfType.containsKey(type)) {
            return pdfType.get(type);
        }
        return null;
    }

    /**
     * 将模版文件加载到本地
     * @param pdfTempFile pdfTempFile
     * @return 文件在本地的名字
     * @throws IOException IOException
     */
    public String loadTempFileToLocal(PDFTempFile pdfTempFile) throws IOException {
        String templateFileParentPath = pdfTempFile.getTemplateFileParentPath();
        String templateFileName = pdfTempFile.getTemplateFileName();

        if (StringUtils.isBlank(templateFileName)) {
            throw AppException.TEMPLATE_FILENAME_NULL_EXCEPTION.build();
        }

        /*
         * 文件父级路径 如果为空 则默认为项目中的指定路径 see {@linkplain SystemConfig#appTemplateFolder}
         */
        templateFileParentPath = StringUtils.isNotBlank(templateFileParentPath) ? templateFileParentPath : systemConfig.getAppTemplateFolder();

        String fullPath = getFullPath(templateFileParentPath, templateFileName);
        String tempName = pdfTempFile.getTemplateFileName();
        if (MyStringUtil.isHttpUrl(fullPath)) {
            /*
             * 生成临时文件 存放路径 see {@linkplain SystemConfig#localTempPath}
             */
            tempName = generateFileNameByTemp(fullPath);
            fullPath = HttpUtil.downloadFile(fullPath, tempName, systemConfig.getLocalTempPath());
        }
        pdfTempFile.setTemplateFileLocalPath(fullPath);
        return tempName;
    }

    /**
     * 根据模版名生成目标文件名
     * @param tempFile 模板文件名
     * @return 目标文件名
     */
    public String generateFileNameByTemp(String tempFile) {
        return generatedKey.generatorKey() + tempFile.substring(tempFile.lastIndexOf("."), tempFile.length());
    }

    /**
     * 获取完整路径
     * @param parent 文件所在的父级
     * @param fileName 文件名
     * @return 完整路径
     */
    public String getFullPath(String parent, String fileName) {
        if (MyStringUtil.isHttpUrl(parent)) {
            // http URL
            if (!parent.endsWith("/"))
                parent =  parent + "/";
        } else {
            // 系统中的路径
            if (!parent.endsWith(File.separator))
                parent = parent + File.separator;
        }
        return parent + fileName;
    }

}
