package com.lifengdi.model.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lifengdi.job.entity.Job;
import com.lifengdi.job.entity.JobCenterFile;
import com.lifengdi.job.entity.RequestParam;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author 李锋镝
 * @date Create at 14:29 2019/4/4
 */
@Data
public class JobDTO {

    /**
     * 任务
     */
    @NotNull(message = "任务不能为空")
    @Valid
    private Job job;

    /**
     * 请求参数
     */
    private List<RequestParam> requestParamList;

    /**
     * 文件
     */
    private List<JobCenterFile> fileList;

    public static void main(String[] args) {
        JobDTO jobTypeDTO = new JobDTO();
        jobTypeDTO.setJob(new Job());
        jobTypeDTO.setRequestParamList(Collections.singletonList(new RequestParam()));
        jobTypeDTO.setFileList(Collections.singletonList(new JobCenterFile()));
        System.out.println(JSON.toJSONString(jobTypeDTO, SerializerFeature.WriteMapNullValue));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "标题" + simpleDateFormat.format(new Date());
        System.out.println(fileName);
    }
}
