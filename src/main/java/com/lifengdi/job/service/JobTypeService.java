package com.lifengdi.job.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lifengdi.enums.GlobalEnum;
import com.lifengdi.enums.ResponseDataEntityEnum;
import com.lifengdi.enums.StatusEnum;
import com.lifengdi.job.entity.JobType;
import com.lifengdi.job.example.JobTypeCondition;
import com.lifengdi.job.mapper.IJobTypeMapper;
import com.lifengdi.model.User;
import com.lifengdi.model.query.JobTypeQuery;
import com.lifengdi.util.GeneratedKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author 李锋镝
 * @date Create at 14:52 2019/4/2
 */
@Service
public class JobTypeService {

    @Resource
    private IJobTypeMapper iJobTypeMapper;

    @Resource
    private GeneratedKey generatedKey;

    /**
     * 新增任务类型
     * @param jobType JobType
     * @param user User
     * @return JobType
     */
    public JobType insert(JobType jobType, User user) {

        Objects.requireNonNull(jobType);

        withCreatedUser(jobType, user);
        iWithDefault(jobType);

        iJobTypeMapper.insertSelective(jobType);
        return jobType;
    }

    /**
     * 赋值CreatedUser
     * @param jobType JobType
     * @param user user
     */
    private void withCreatedUser(JobType jobType, User user) {
        if (Objects.nonNull(user)) {
            jobType.setCreatedUserId(user.getUserId());
            jobType.setCreatedUserPhone(user.getPhone());
            jobType.setCreatedUserName(user.getUserName());
        }
    }

    private void iWithDefault(JobType jobType) {

        jobType.setDeleted(GlobalEnum.NO.getValue());
        jobType.setStatus(StatusEnum.JOB_TYPE_AUDITED.getStatus());
        if (StringUtils.isBlank(jobType.getJobTypeId())) {
            jobType.setJobTypeId(generatedKey.generatorKey());
        }
        if (StringUtils.isBlank(jobType.getResponseDataEntity())) {
            jobType.setResponseDataEntity(ResponseDataEntityEnum.PAGE_HELPER_PAGE_INFO.getClassPath());
        }

    }

    /**
     * 更新
     * @param jobType JobType
     * @param user user
     * @return JobType
     */
    public JobType update(JobType jobType, User user) {

        Objects.requireNonNull(jobType);

        withUpdatedUser(jobType, user);
        iWithDefault(jobType);

        iJobTypeMapper.updateByJobTypeIdSelective(jobType);
        return jobType;
    }

    /**
     * 赋值UpdatedUser
     * @param jobType JobType
     * @param user user
     */
    private void withUpdatedUser(JobType jobType, User user) {
        if (Objects.nonNull(user)) {
            jobType.setUpdatedUserId(user.getUserId());
            jobType.setUpdatedUserPhone(user.getPhone());
            jobType.setUpdatedUserName(user.getUserName());
        }
    }

    /**
     * 列表查询
     * @param jobTypeQuery JobTypeQuery
     * @return PageInfo<JobType>
     */
    public PageInfo<JobType> list(JobTypeQuery jobTypeQuery) {

        JobTypeCondition condition = new JobTypeCondition();
        JobTypeCondition.Criteria criteria = condition.createCriteria();

        if (StringUtils.isNotBlank(jobTypeQuery.getCreatedUserId())) {
            criteria.andCreatedUserIdEqualTo(jobTypeQuery.getCreatedUserId());
        }

        if (StringUtils.isNotBlank(jobTypeQuery.getCreatedUserPhone())) {
            criteria.andCreatedUserPhoneEqualTo(jobTypeQuery.getCreatedUserPhone());
        }

        if (StringUtils.isNotBlank(jobTypeQuery.getStatus())) {
            criteria.andStatusEqualTo(jobTypeQuery.getStatus());
        }

        if (StringUtils.isNotBlank(jobTypeQuery.getRequestMethod())) {
            criteria.andRequestMethodEqualTo(jobTypeQuery.getRequestMethod());
        }

        if (Objects.nonNull(jobTypeQuery.getDeleted())) {
            criteria.andDeletedEqualTo(jobTypeQuery.getDeleted());
        }

        if (Objects.nonNull(jobTypeQuery.getJobTypeDesc())) {
            criteria.andJobTypeDescLike("%" + jobTypeQuery.getJobTypeDesc() + "%");
        }

        if (Objects.nonNull(jobTypeQuery.getOrderBy())) {
            condition.setOrderByClause(jobTypeQuery.getOrderBy());
        } else {
            condition.setOrderByClause("created_time desc");
        }

        PageHelper.startPage(jobTypeQuery.getPage(), jobTypeQuery.getSize());

        List<JobType> jobTypeList = iJobTypeMapper.selectByExample(condition);

        return new PageInfo<>(jobTypeList);
    }

    /**
     * 根据JobTypeId获取详情
     * @param jobTypeId jobTypeId
     * @return JobType
     */
    public JobType get(String jobTypeId) {
        if (StringUtils.isBlank(jobTypeId)) {
            return null;
        }
        return iJobTypeMapper.selectByJobTypeId(jobTypeId);
    }

    public void delete(String jobTypeId, User user) {
        JobTypeCondition condition = new JobTypeCondition();
        condition.createCriteria().andJobTypeIdEqualTo(jobTypeId);

        JobType jobType = new JobType();
        jobType.setDeleted(GlobalEnum.YES.getValue());
        withUpdatedUser(jobType, user);

        iJobTypeMapper.updateByExampleSelective(jobType, condition);
    }
}
