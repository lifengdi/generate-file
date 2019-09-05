package com.lifengdi.job.mapper;


import com.lifengdi.job.entity.JobType;

/**
 * 任务类型
 * 
 * @author goddess
 * 2019-04-03
 */
public interface IJobTypeMapper extends JobTypeMapper {


    /**
     * 动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record JobType
     * 
     * @return int 影响行数
     */
    int updateByJobTypeIdSelective(JobType record);


    /**
     * 根据JobTypeId获取详情
     * @param jobTypeId jobTypeId
     * @return JobType
     */
    JobType selectByJobTypeId(String jobTypeId);

}