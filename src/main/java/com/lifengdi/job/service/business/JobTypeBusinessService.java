package com.lifengdi.job.service.business;

import com.github.pagehelper.PageInfo;
import com.lifengdi.job.entity.JobType;
import com.lifengdi.model.User;
import com.lifengdi.model.dto.JobTypeDTO;
import com.lifengdi.model.query.JobTypeQuery;

/**
 * @author 李锋镝
 * @date Create at 16:39 2019/4/2
 */
public interface JobTypeBusinessService {
    /**
     * 保存JobType
     * @param jobTypeDTO JobTypeDTO
     * @param user 当前用户
     */
    void insert(JobTypeDTO jobTypeDTO, User user);

    /**
     * 修改JobType
     * @param jobTypeDTO JobTypeDTO
     * @param user 当前用户
     */
    void update(JobTypeDTO jobTypeDTO, User user);

    /**
     * 查询
     * @param jobTypeQuery JobTypeQuery
     * @return PageInfo
     */
    PageInfo<JobType> list(JobTypeQuery jobTypeQuery);

    /**
     * 根据jobTypeId获取详情
     * @param jobTypeId jobTypeId
     * @return JobType
     */
    JobType detail(String jobTypeId);

    /**
     * 删除任务
     * @param jobTypeId jobTypeId
     * @param user user
     */
    void delete(String jobTypeId, User user);
}
