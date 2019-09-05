package com.lifengdi.job.service.business;

import com.github.pagehelper.PageInfo;
import com.lifengdi.job.entity.Job;
import com.lifengdi.model.User;
import com.lifengdi.model.dto.JobDTO;
import com.lifengdi.model.dto.JobListDTO;
import com.lifengdi.model.query.JobQuery;

/**
 * @author 李锋镝
 * @date Create at 14:20 2019/4/4
 */
public interface JobBusinessService {

    /**
     * 新增job
     * @param jobDTO JobDTO
     * @param user User
     */
    void insert(JobDTO jobDTO, User user);

    /**
     * 查询
     * @param jobQuery jobQuery see{@linkplain JobQuery}
     * @return PageInfo
     */
    PageInfo<JobListDTO> list(JobQuery jobQuery);

    /**
     * 详情
     * @param jobId jobId
     * @return Job
     */
    Job get(String jobId);

    /**
     * 执行任务
     */
    void executeJob();
}
