package com.lifengdi.controller;

import com.github.pagehelper.PageInfo;
import com.lifengdi.constant.LockKey;
import com.lifengdi.enums.ObjectTypeEnum;
import com.lifengdi.exception.AppException;
import com.lifengdi.job.service.FileService;
import com.lifengdi.job.service.RequestParamService;
import com.lifengdi.job.service.business.JobBusinessService;
import com.lifengdi.model.dto.JobDTO;
import com.lifengdi.model.dto.JobListDTO;
import com.lifengdi.model.query.JobQuery;
import com.lifengdi.response.ResponseResult;
import com.lifengdi.util.DistributedLocks;
import com.lifengdi.util.GeneratedKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

/**
 * 任务
 * @author 李锋镝
 * @date Create at 09:49 2019/4/8
 */
@RestController
@RequestMapping("/job")
public class JobController extends BaseController {

    @Resource
    private JobBusinessService jobBusinessService;

    @Resource
    private RequestParamService requestParamService;

    @Resource
    private FileService fileService;

    @Resource
    private DistributedLocks distributedLocks;

    @Resource
    private GeneratedKey generatedKey;

    @PostMapping("/insert")
    public ResponseResult insert(@RequestBody @Valid JobDTO jobDTO) {
        jobBusinessService.insert(jobDTO, getCurrentUser());
        return ResponseResult.success("");
    }

    @PostMapping("/list")
    public ResponseResult list(@RequestBody JobQuery jobQuery) {
        if (StringUtils.isBlank(jobQuery.getCreatedUserId())) {
            jobQuery.setCreatedUserId(getCurrentUser().getUserId());
        }
        PageInfo<JobListDTO> pageInfo = jobBusinessService.list(jobQuery);
        return ResponseResult.success(pageInfo);
    }

    @GetMapping("/{jobId}/detail")
    public ResponseResult detail(@PathVariable("jobId") String jobId) {
        if (StringUtils.isBlank(jobId)) {
            throw AppException.JOB_ID_CAN_NOT_BE_NULL.build();
        }
        JobDTO jobDTO = new JobDTO();
        jobDTO.setJob(jobBusinessService.get(jobId));
        jobDTO.setRequestParamList(requestParamService.list(jobId, ObjectTypeEnum.JOB));
        jobDTO.setFileList(fileService.listWithTempUrl(jobId, ObjectTypeEnum.JOB));
        return ResponseResult.success(jobDTO);
    }

    @GetMapping("/execute-job")
    public ResponseResult executeJob() {
        String key = LockKey.getKeyByFormat(generatedKey.getProjectIdentifier(), LockKey.JOB_CENTER_EXECUTE_JOB_SERVER_LOCK);
        if (distributedLocks.sixtyMinutesLock(key)) {
            try {
                CompletableFuture.runAsync(() -> jobBusinessService.executeJob());
                return ResponseResult.success("");
            } finally {
                distributedLocks.unlock(key);
            }
        }
        return ResponseResult.success("当前节点正在处理任务中。节点ID：" + generatedKey.getProjectIdentifier());
    }
}
