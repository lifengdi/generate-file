package com.lifengdi.file.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.lifengdi.config.GlobalConfig;
import com.lifengdi.config.PDFFontConfig;
import com.lifengdi.config.SystemConfig;
import com.lifengdi.file.pdf.listener.MyTextLocationListener;
import com.lifengdi.model.pdf.Location;
import com.lifengdi.model.pdf.PDFStamperConfig;
import com.lifengdi.model.pdf.PDFTempFile;
import com.lifengdi.model.pdf.PDFWatermarkConfig;
import com.lifengdi.util.GeneratedKey;
import com.lifengdi.util.MyStringUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.Resource;
import java.io.*;
import java.util.Objects;

/**
 * 生成PDF工具类
 *
 * @author 李锋镝
 * @date Create at 10:42 2019/5/9
 */
@Component
@Slf4j
public class HtmlToPDF {

    @Resource
    private SystemConfig systemConfig;

    @Resource
    private GeneratedKey generatedKey;

    /**
     * 模板和数据匹配
     *
     * @param data        数据
     * @param pdfTempFile pdfTempFile
     * @return html
     */
    public String matchDataToHtml(Object data, PDFTempFile pdfTempFile) {
        StringWriter writer = new StringWriter();
        String html;
        try {
            // FreeMarker配置
            Configuration config = new Configuration(Configuration.VERSION_2_3_25);
            config.setDefaultEncoding(GlobalConfig.DEFAULT_ENCODING);
            // 注意这里是模板所在文件夹，不是模版文件
            String parentPath, tempFileName = pdfTempFile.getTemplateFileName();
            if (MyStringUtil.isHttpUrl(pdfTempFile.getTemplateFileParentPath())) {
                parentPath = systemConfig.getLocalTempPath();
            } else {
                parentPath = pdfTempFile.getTemplateFileParentPath();
                // 将项目中的文件copy到服务器本地
                if (!parentPath.endsWith(File.separator))
                    parentPath = parentPath + File.separator;
                String localTempPath = systemConfig.getLocalTempPath();
                String target = (localTempPath.endsWith(File.separator) ? localTempPath : localTempPath + File.separator) + tempFileName;
                InputStream tempFileInputStream = new ClassPathResource(parentPath + tempFileName).getInputStream();
                FileUtils.copyInputStreamToFile(tempFileInputStream, new File(target));
            }
            log.info("模板和数据匹配,模板文件parentPath:{}", parentPath);

            config.setDirectoryForTemplateLoading(new File(systemConfig.getLocalTempPath()));
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            config.setLogTemplateExceptions(false);
            // 根据模板名称 获取对应模板
            Template template = config.getTemplate(tempFileName);
            // 模板和数据的匹配
            template.process(data, writer);
            writer.flush();
            html = writer.toString();
            return html;
        } catch (Exception e) {
            log.error("PDF模板和数据匹配异常", e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                log.error("StringWriter close exception.", e);
            }
        }
        return null;
    }


    /**
     * 生成PDF
     *
     * @param html           html字符串
     * @param targetFileName 目标文件名
     * @return 生成文件的名称
     * @throws Exception e
     */
    public String createPDF(String html, String targetFileName) throws Exception {
        if (StringUtils.isBlank(html)) {
            return null;
        }
        targetFileName = getFileName(targetFileName);
        log.info("生成PDF,targetFileName:{}", targetFileName);
        String targetFilePath = getTargetFileTempPath(targetFileName);
        FileOutputStream outFile = new FileOutputStream(targetFilePath);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        // 解决中文支持问题
        log.info("加载字体");
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(SystemConfig.SourceHanSansCN_Regular_TTF, "SourceHanSansCN", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, null);
        fontResolver.addFont(SystemConfig.FONT_PATH_SONG, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        renderer.layout();
        renderer.createPDF(outFile);
        log.info("生成PDF,targetFileName:{},生成成功", targetFileName);
        return targetFileName;
    }

    /**
     * 渲染文件
     *
     * @param filePath           PDF文件路径
     * @param outFilePath        PDF文件路径
     * @param pdfStamperConfig   pdfStamperConfig
     * @param pdfWatermarkConfig pdfWatermarkConfig
     */
    public void renderLayer(String filePath, String outFilePath, PDFStamperConfig pdfStamperConfig, PDFWatermarkConfig pdfWatermarkConfig) {

        log.info("渲染文件,filePath:{},outFilePath:{}", filePath, outFilePath);
        InputStream inputStream = null;
        try {
            filePath = getTargetFileTempPath(filePath);
            inputStream = new FileInputStream(filePath);
            PdfReader reader = new PdfReader(inputStream);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(getTargetFileTempPath(outFilePath)));
            if (Objects.nonNull(pdfStamperConfig) && pdfStamperConfig.getGenerate()) {
                // 印章
                log.info("添加印章,pdfStamperConfig:{}", pdfStamperConfig);
                stamper(pdfStamperConfig, reader, stamper);
            }
            if (Objects.nonNull(pdfWatermarkConfig) && pdfWatermarkConfig.getGenerate()) {
                // 水印
                log.info("添加水印,pdfWatermarkConfig:{}", pdfWatermarkConfig);
                watermark(reader, stamper, pdfWatermarkConfig);
            }
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 印章
     *
     * @param pdfStamperConfig pdfStamperConfig
     * @param reader           reader
     * @param stamper          stamper
     * @throws IOException       IOException
     * @throws DocumentException DocumentException
     */
    private void stamper(PDFStamperConfig pdfStamperConfig, PdfReader reader, PdfStamper stamper) throws IOException, DocumentException {
        Image image;
        if (!MyStringUtil.isHttpUrl(pdfStamperConfig.getStamperUrl())) {
            // 将项目中的文件copy到服务器本地
            String imagePath = pdfStamperConfig.getStamperUrl();
            String imageName = imagePath.substring(imagePath.indexOf("/") + 1, imagePath.length());
            String localTempPath = systemConfig.getLocalTempPath();
            String target = (localTempPath.endsWith(File.separator) ? localTempPath : localTempPath + File.separator) + imageName;
            InputStream tempFileInputStream = new ClassPathResource(pdfStamperConfig.getStamperUrl()).getInputStream();
            FileUtils.copyInputStreamToFile(tempFileInputStream, new File(target));
            image = Image.getInstance(target);
        } else {
            image = Image.getInstance(pdfStamperConfig.getStamperUrl());
        }

        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        Document document = new Document();
        log.info("A4纸width:{},height:{}", document.getPageSize().getWidth(), document.getPageSize().getHeight());
//            // 取所在页和坐标，左下角为起点
//            float x = document.getPageSize().getWidth() - 240;
//            float y = document.getPageSize().getHeight() - 680;
        int pageSize = reader.getNumberOfPages();
        Location location = pdfStamperConfig.getLocation();
        if (Objects.nonNull(location)) {
            Integer page = location.getPage();// -1:全部，0:最后一页，1:首页
            if (Objects.nonNull(page)) {
                switch (page) {
                    case -1:
                        for (int pageNumber = 1; pageNumber <= pageSize; pageNumber++) {
                            insertImage(pdfStamperConfig, stamper, image, parser, pageSize);
                        }
                        break;
                    case 0:
                        insertImage(pdfStamperConfig, stamper, image, parser, pageSize);
                        break;
                    default:
                        if (page > 0) {
                            insertImage(pdfStamperConfig, stamper, image, parser, page);
                        }
                        break;
                }
            }

        }
    }

    /**
     * 向指定位置插入图片
     *
     * @param pdfStamperConfig pdfStamperConfig
     * @param stamper          stamper
     * @param image            image
     * @param parser           parser
     * @param pageNumber       pageNumber
     * @throws IOException       IOException
     * @throws DocumentException DocumentException
     */
    private void insertImage(PDFStamperConfig pdfStamperConfig, PdfStamper stamper, Image image, PdfReaderContentParser parser, int pageNumber)
            throws IOException, DocumentException {
        float x, y;
        Location xy = new Location();
        if (StringUtils.isNotBlank(pdfStamperConfig.getLocation().getWord())) {
            parser.processContent(pageNumber, new MyTextLocationListener(pdfStamperConfig, xy));
        } else {
            xy = pdfStamperConfig.getLocation();
        }
        if (Objects.isNull(xy)) {
            return;
        }

        if (Objects.isNull(xy.getX()) || Objects.isNull(xy.getY())) {
            return;
        }
        x = xy.getX();
        y = xy.getY();
        if (x < 0 || y < 0) {
            return;
        }
        // 读图片
        // 获取操作的页面
        PdfContentByte under = stamper.getOverContent(pageNumber);
//         根据域的大小缩放图片
//        image.scaleToFit(Objects.isNull(pdfStamperConfig.getFitWidth()) ? GlobalConfig.PDF_STAMPER_DEFAULT_FIT_WIDTH : pdfStamperConfig.getFitWidth(),
//                Objects.isNull(pdfStamperConfig.getFitHeight()) ? GlobalConfig.PDF_STAMPER_DEFAULT_FIT_HEIGHT : pdfStamperConfig.getFitHeight());
        image.scaleAbsolute(Objects.isNull(pdfStamperConfig.getFitWidth()) ? GlobalConfig.PDF_STAMPER_DEFAULT_FIT_WIDTH : pdfStamperConfig.getFitWidth(),
                Objects.isNull(pdfStamperConfig.getFitHeight()) ? GlobalConfig.PDF_STAMPER_DEFAULT_FIT_HEIGHT : pdfStamperConfig.getFitHeight());
        // 添加图片
        image.setAbsolutePosition(x, y);
        under.addImage(image);
    }

    /**
     * 水印
     *
     * @param reader             reader
     * @param stamper            stamper
     * @param pdfWatermarkConfig pdfWatermarkConfig
     */
    private void watermark(PdfReader reader, PdfStamper stamper, PDFWatermarkConfig pdfWatermarkConfig) {
        if (Objects.isNull(pdfWatermarkConfig) || !pdfWatermarkConfig.getGenerate()) {
            return;
        }
        PdfContentByte under;
        // 字体
        BaseFont font = PDFFontConfig.FONT_MAP.get(PDFFontConfig.SIM_SUN);
        String fontFamily = pdfWatermarkConfig.getFontFamily();
        if (StringUtils.isNotBlank(fontFamily) && PDFFontConfig.FONT_MAP.containsKey(fontFamily)) {
            font = PDFFontConfig.FONT_MAP.get(fontFamily);
        }
        // 原pdf文件的总页数
        int pageSize = reader.getNumberOfPages();
        PdfGState gs = new PdfGState();
        // 设置填充字体不透明度为0.1f
        gs.setFillOpacity(0.1f);
        Document document = new Document();
        float documentWidth = document.getPageSize().getWidth(), documentHeight = document.getPageSize().getHeight();
        Location location = pdfWatermarkConfig.getLocation();
        if (Objects.isNull(location)) {
            location = new Location();
        }
        final float xStart = 0, yStart = 0,
                xInterval = Objects.nonNull(location.getXInterval()) ? location.getXInterval() : GlobalConfig.DEFAULT_X_INTERVAL,
                yInterval = Objects.nonNull(location.getYInterval()) ? location.getYInterval() : GlobalConfig.DEFAULT_Y_INTERVAL,
                rotation = 45,
                fontSize = Objects.isNull(pdfWatermarkConfig.getFontSize()) ? GlobalConfig.DEFAULT_FONT_SIZE : pdfWatermarkConfig.getFontSize();

        String watermarkWord = pdfWatermarkConfig.getWatermarkWord();
        int red = -1, green = -1, blue = -1;
        String[] colorArray = pdfWatermarkConfig.getWatermarkColor().split(",");
        if (colorArray.length >= 3) {
            red = Integer.parseInt(colorArray[0]);
            green = Integer.parseInt(colorArray[1]);
            blue = Integer.parseInt(colorArray[2]);
        }
        for (int i = 1; i <= pageSize; i++) {
            // 水印在之前文本下
            if (Objects.nonNull(location.getOverContent()) && location.getOverContent()) {
                under = stamper.getOverContent(i);
            } else {
                under = stamper.getUnderContent(i);
            }
            under.beginText();
            // 文字水印 颜色
            if (red >= 0) {
                under.setColorFill(new BaseColor(red, green, blue));
            } else {
                under.setColorFill(BaseColor.GRAY);
            }
            // 文字水印 字体及字号
            under.setFontAndSize(font, fontSize);
            under.setGState(gs);
            // 文字水印 起始位置
            under.setTextMatrix(xStart, yStart);

            if (StringUtils.isNotBlank(watermarkWord)) {
                for (float x = xStart; x <= documentWidth + xInterval; x += xInterval) {
                    for (float y = yStart; y <= documentHeight + yInterval; y += yInterval) {
                        under.showTextAligned(Element.ALIGN_CENTER, watermarkWord, x, y, rotation);
                    }
                }
            }
            under.endText();
        }
    }

    /**
     * 获取生成的PDF文件本地临时路径
     *
     * @param targetFileName 目标文件名
     * @return 本地临时路径
     */
    public String getTargetFileTempPath(String targetFileName) {

        String localTempPath = systemConfig.getLocalTempPath();
        if (!localTempPath.endsWith(File.separator)) {
            localTempPath = localTempPath + File.separator;
        }
        return localTempPath + targetFileName;
    }

    private String getFileName(String targetFileName) {
        if (StringUtils.isBlank(targetFileName)) {
            targetFileName = generatedKey.generatorKey();
        }
        if (!StringUtils.endsWithIgnoreCase(targetFileName, GlobalConfig.PDF_SUFFIX)) {
            targetFileName = targetFileName + GlobalConfig.PDF_SUFFIX;
        }
        return targetFileName;
    }
}
