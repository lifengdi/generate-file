package com.lifengdi.job.service.business;

import com.github.pagehelper.PageInfo;
import com.lifengdi.job.entity.JobLog;
import com.lifengdi.model.query.JobLogQuery;

/**
 * @author 李锋镝
 * @date Create at 15:15 2019/4/8
 */
public interface JobLogBusinessService {

    /**
     * 查询
     * @param jobLogQuery jobQuery see{@linkplain JobLogQuery}
     * @return PageInfo
     */
    PageInfo<JobLog> list(JobLogQuery jobLogQuery);

    /**
     * 详情
     * @param id
     * @return
     */
    JobLog get(long id);
}
