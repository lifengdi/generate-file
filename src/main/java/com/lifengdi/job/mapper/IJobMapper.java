package com.lifengdi.job.mapper;

import com.lifengdi.job.entity.Job;

/**
 * 任务类型
 * 
 * @author goddess
 * 2019-04-04
 */
public interface IJobMapper extends JobMapper{

    /**
     * 根据指定主键获取一条数据库记录
     *
     * @param jobId jobId
     * 
     * @return Job
     */
    Job selectByJobId(String jobId);

}