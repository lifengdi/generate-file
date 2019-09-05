package com.lifengdi.controller;

import com.github.pagehelper.PageInfo;
import com.lifengdi.job.entity.JobLog;
import com.lifengdi.job.service.business.JobLogBusinessService;
import com.lifengdi.model.query.JobLogQuery;
import com.lifengdi.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 任务
 * @author 李锋镝
 * @date Create at 09:49 2019/4/8
 */
@RestController
@RequestMapping("/job-log")
public class JobLogController extends BaseController {

    @Resource
    private JobLogBusinessService jobLogBusinessService;

    @PostMapping("/list")
    public ResponseResult list(@RequestBody JobLogQuery jobLogQuery) {
        PageInfo<JobLog> pageInfo = jobLogBusinessService.list(jobLogQuery);
        return ResponseResult.success(pageInfo);
    }

    @GetMapping("/{jobLogId}/detail")
    public ResponseResult detail(@PathVariable("jobLogId") Long id) {
        JobLog jobLog = jobLogBusinessService.get(id);
        return ResponseResult.success(jobLog);
    }
}
