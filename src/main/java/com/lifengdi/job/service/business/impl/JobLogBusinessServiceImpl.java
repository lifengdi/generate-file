package com.lifengdi.job.service.business.impl;

import com.github.pagehelper.PageInfo;
import com.lifengdi.job.entity.JobLog;
import com.lifengdi.job.service.LogService;
import com.lifengdi.job.service.business.JobLogBusinessService;
import com.lifengdi.model.query.JobLogQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 李锋镝
 * @date Create at 15:16 2019/4/8
 */
@Service
public class JobLogBusinessServiceImpl implements JobLogBusinessService {

    @Resource
    private LogService logService;

    @Override
    public PageInfo<JobLog> list(JobLogQuery jobLogQuery) {
        return logService.listJobLog(jobLogQuery);
    }

    @Override
    public JobLog get(long id) {
        return logService.detailJobLog(id);
    }
}
