package com.lifengdi.controller;

import com.github.pagehelper.PageInfo;
import com.lifengdi.enums.ObjectTypeEnum;
import com.lifengdi.exception.AppException;
import com.lifengdi.job.entity.JobType;
import com.lifengdi.job.service.FileService;
import com.lifengdi.job.service.RequestParamService;
import com.lifengdi.job.service.business.JobTypeBusinessService;
import com.lifengdi.model.dto.JobTypeDTO;
import com.lifengdi.model.query.JobTypeQuery;
import com.lifengdi.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author 李锋镝
 * @date Create at 14:47 2019/4/2
 */
@RestController
@RequestMapping("/job-type")
public class JobTypeController extends BaseController{

    @Resource
    private JobTypeBusinessService jobTypeBusinessService;

    @Resource
    private RequestParamService requestParamService;

    @Resource
    private FileService fileService;

    @PostMapping("/insert")
    public ResponseResult insert(@RequestBody @Valid JobTypeDTO jobTypeDTO) {
        jobTypeBusinessService.insert(jobTypeDTO, getCurrentUser());
        return ResponseResult.success("");
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody @Valid JobTypeDTO jobTypeDTO) {
        if (StringUtils.isBlank(jobTypeDTO.getJobType().getJobTypeId())) {
            throw AppException.JOB_TYPE_NOT_FOUND_ID.build();
        }
        jobTypeBusinessService.update(jobTypeDTO, getCurrentUser());
        return ResponseResult.success("");
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody JobTypeQuery jobTypeQuery) {
        PageInfo<JobType> pageInfo = jobTypeBusinessService.list(jobTypeQuery);
        return ResponseResult.success(pageInfo);
    }

    @GetMapping("/{jobTypeId}/detail")
    public ResponseResult detail(@PathVariable("jobTypeId") String jobTypeId) {
        if (StringUtils.isBlank(jobTypeId)) {
            throw AppException.JOB_TYPE_ID_CAN_NOT_BE_NULL.build();
        }
        JobType jobType = jobTypeBusinessService.detail(jobTypeId);
        JobTypeDTO jobTypeDTO = new JobTypeDTO();
        jobTypeDTO.setJobType(jobType);
        jobTypeDTO.setRequestParamList(requestParamService.list(jobTypeId, ObjectTypeEnum.JOB_TYPE));
        jobTypeDTO.setFileList(fileService.list(jobTypeId, ObjectTypeEnum.JOB_TYPE));
        return ResponseResult.success(jobTypeDTO);
    }

    @GetMapping("/{jobTypeId}/delete")
    public ResponseResult delete(@PathVariable("jobTypeId") String jobTypeId) {
        if (StringUtils.isBlank(jobTypeId)) {
            throw AppException.JOB_TYPE_ID_CAN_NOT_BE_NULL.build();
        }
        jobTypeBusinessService.delete(jobTypeId, getCurrentUser());
        return ResponseResult.success("");
    }
}
