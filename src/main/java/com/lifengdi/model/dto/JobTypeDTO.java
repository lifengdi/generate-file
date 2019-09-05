package com.lifengdi.model.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lifengdi.job.entity.JobCenterFile;
import com.lifengdi.job.entity.JobType;
import com.lifengdi.job.entity.RequestParam;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * @author 李锋镝
 * @date Create at 14:43 2019/4/2
 */
@Data
public class JobTypeDTO {
    /**
     * 任务类型
     */
    @Valid
    @NotNull(message = "请填写需要保存的任务类型")
    private JobType jobType;

    /**
     * 请求参数
     */
    private List<RequestParam> requestParamList;

    /**
     * 文件
     */
    private List<JobCenterFile> fileList;

    public static void main(String[] args) {
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobType(new JobType());
        jobTypeDTO.setRequestParamList(Collections.singletonList(new RequestParam()));
        jobTypeDTO.setFileList(Collections.singletonList(new JobCenterFile()));
        System.out.println(JSON.toJSONString(jobTypeDTO, SerializerFeature.WriteMapNullValue));
    }
}
