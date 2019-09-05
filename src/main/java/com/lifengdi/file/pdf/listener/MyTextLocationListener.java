package com.lifengdi.file.pdf.listener;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.lifengdi.config.GlobalConfig;
import com.lifengdi.model.pdf.Location;
import com.lifengdi.model.pdf.PDFStamperConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author 李锋镝
 * @date Create at 15:32 2019/5/9
 */
@Slf4j
public class MyTextLocationListener implements RenderListener {

    private String text;

    private PDFStamperConfig pdfStamperConfig;

    private Location location;

    public MyTextLocationListener(PDFStamperConfig pdfStamperConfig, Location location) {
        if (StringUtils.isNotBlank(pdfStamperConfig.getLocation().getWord())) {
            this.text = pdfStamperConfig.getLocation().getWord();
        }

        this.pdfStamperConfig = pdfStamperConfig;
        this.location = location;
    }

    @Override
    public void beginTextBlock() {
    }

    @Override
    public void renderText(TextRenderInfo renderInfo) {
        String renderInfoText = renderInfo.getText();
        if (!StringUtils.isEmpty(renderInfoText) && renderInfoText.contains(text)) {
            Rectangle2D.Float base = renderInfo.getBaseline().getBoundingRectange();
            float leftX = (float) base.getMinX();
            float leftY = (float) base.getMinY() - 1;
            float rightX = (float) base.getMaxX();
            float rightY = (float) base.getMaxY() + 1;
            Rectangle2D.Float rect = new Rectangle2D.Float(leftX, leftY, rightX - leftX, rightY - leftY);

            // 当前行长度
            int length = renderInfoText.length();
            // 单个字符的长度
            float wordWidth = rect.width / length;
            // 指定字符串首次出现的索引
            int i = renderInfoText.indexOf(text);

            Float fitWidth = Objects.isNull(pdfStamperConfig.getFitWidth()) ? GlobalConfig.PDF_STAMPER_DEFAULT_FIT_WIDTH : pdfStamperConfig.getFitWidth();
            Float fitHeight = Objects.isNull(pdfStamperConfig.getFitHeight()) ? GlobalConfig.PDF_STAMPER_DEFAULT_FIT_HEIGHT : pdfStamperConfig.getFitHeight();
            float fitWidthRadius = 0f, fitHeightRadius = 0f;
            if (fitWidth > 0) {
                fitWidthRadius = fitWidth / 2;
            }
            if (fitHeight > 0) {
                fitHeightRadius = fitHeight / 2;
            }
            // 偏移量
            Float xOffset = Objects.isNull(pdfStamperConfig.getXOffset()) ? 0F : pdfStamperConfig.getXOffset();
            Float yOffset = Objects.isNull(pdfStamperConfig.getYOffset()) ? 0F : pdfStamperConfig.getYOffset();
            // 设置印章的XY坐标
            float x, y;
            if (rect.x < 60) {
                x = wordWidth * i + fitHeightRadius + xOffset;
            } else {
                x = rect.x + xOffset;
            }
            y = rect.y - fitWidthRadius + yOffset;
            location.setY(y > 0 ? y : 0);
            location.setX(x > 0 ? x : 0);
            log.info("text:{}, location:{}", text, location);
        }
    }

    @Override
    public void endTextBlock() {
    }

    @Override
    public void renderImage(ImageRenderInfo renderInfo) {
    }
}
