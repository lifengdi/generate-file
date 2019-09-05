package com.lifengdi.controller;

import com.lifengdi.enums.*;
import com.lifengdi.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李锋镝
 * @date Create at 17:08 2019/4/16
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @GetMapping("/enums")
    public ResponseResult enums() {
        Map<String, Object> map = new HashMap<>();
        map.put("fileAnalysisModelEnum", FileAnalysisModelEnum.keyValue());
        map.put("fileTypeEnum", FileTypeEnum.keyValue());
        map.put("globalEnum", GlobalEnum.keyValue());
        map.put("paramTypeEnum", ParamTypeEnum.keyValue());
        map.put("requestMethodEnum", RequestMethodEnum.keyValue());
        map.put("responseDataEntityEnum", ResponseDataEntityEnum.keyValue());
        map.put("statusEnum", StatusEnum.keyValue());
        return ResponseResult.success(map);
    }

}
