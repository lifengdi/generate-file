package com.lifengdi.model.pdf;

import com.lifengdi.model.IType;
import lombok.Data;

/**
 * @author 李锋镝
 * @date Create at 14:07 2019/5/9
 */
@Data
public class PDFType implements IType {

    private PDFTempFile pdfTempFile;

    private PDFStamperConfig pdfStamperConfig;

    private PDFWatermarkConfig pdfWatermarkConfig;
}
