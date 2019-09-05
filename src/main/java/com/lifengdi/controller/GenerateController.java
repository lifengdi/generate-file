package com.lifengdi.controller;

import com.lifengdi.commons.utils.Either;
import com.lifengdi.exception.ApiException;
import com.lifengdi.file.builder.HtmlToPDFBuilder;
import com.lifengdi.file.builder.InnerDownloadBuilder;
import com.lifengdi.model.dto.DownloadFileDTO;
import com.lifengdi.model.PdfDTO;
import com.lifengdi.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author 李锋镝
 * @date Create at 14:37 2019/5/13
 */
@RestController
@RequestMapping("/generate")
@Slf4j
public class GenerateController extends BaseController {

    @Resource
    private HtmlToPDFBuilder htmlToPDFBuilder;

    @Resource
    private InnerDownloadBuilder innerDownloadBuilder;


    /**
     * 生成PDF
     * @param pdfDTO PDF参数
     * @return ResponseResult
     */
    @PostMapping("/pdf")
    public ResponseResult pdf(@RequestBody @Valid PdfDTO pdfDTO) {
        Either<ApiException, String> either =  htmlToPDFBuilder.builder(pdfDTO.getTargetName(),
                pdfDTO.getType(), pdfDTO.getData());
        return either.fold(ResponseResult::fail, ResponseResult::success);
    }

    /**
     * 下载文件到私有存储
     * @param dto DownloadFileDTO
     * @return ResponseResult
     */
    @PostMapping("/inner-download")
    public ResponseResult innerDownloadFile(@RequestBody @Valid DownloadFileDTO dto) {

        return innerDownloadBuilder.batchDownload(dto)
                .fold(ResponseResult::fail, ResponseResult::success);
    }
}
