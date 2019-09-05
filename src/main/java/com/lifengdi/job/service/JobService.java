package com.lifengdi.job.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lifengdi.enums.FileTypeEnum;
import com.lifengdi.enums.GlobalEnum;
import com.lifengdi.enums.ObjectTypeEnum;
import com.lifengdi.enums.StatusEnum;
import com.lifengdi.exception.AppException;
import com.lifengdi.job.entity.Job;
import com.lifengdi.job.entity.JobCenterFile;
import com.lifengdi.job.example.JobCondition;
import com.lifengdi.job.mapper.IJobMapper;
import com.lifengdi.model.User;
import com.lifengdi.model.dto.JobListDTO;
import com.lifengdi.model.query.JobQuery;
import com.lifengdi.util.GeneratedKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 李锋镝
 * @date Create at 14:08 2019/4/4
 */
@Service
@Slf4j
public class JobService {

    @Resource
    private IJobMapper iJobMapper;

    @Resource
    private GeneratedKey generatedKey;

    @Resource
    private FileService fileService;

    /**
     * 新增job
     * @param job Job
     * @param user User
     * @return Job
     */
    public Job insert(Job job, User user) {

        Objects.requireNonNull(job);

        if (StringUtils.isBlank(job.getJobTitle())) {
            throw AppException.JOB_TITLE_CAN_NOT_BE_NULL.build();
        }
        if (StringUtils.isBlank(job.getJobTypeId())) {
            throw AppException.JOB_WITHOUT_JOB_TYPE.build();
        }

        iWithDefault(job, user);
        withUpdateUser(job, user);
        iJobMapper.insertSelective(job);
        log.info("新增JOB，job:{}", job);
        return job;
    }

    private void iWithDefault(Job job, User user) {
        withCreatedUser(job, user);
        job.setExecuteStatus(StatusEnum.JOB_EXECUTE_STATUS_WAIT.getStatus());
        job.setStatus(StatusEnum.COMMON_AUDITED.getStatus());
        job.setDeleted(GlobalEnum.NO.getValue());
        if (StringUtils.isBlank(job.getJobId())) {
            job.setJobId(generatedKey.generatorKey());
        }
    }

    private void withCreatedUser(Job job, User user) {
        if (Objects.nonNull(user)) {
            job.setCreatedUserId(user.getUserId());
            job.setCreatedUserName(user.getUserName());
            job.setCreatedUserPhone(user.getPhone());
        }
    }

    private void withUpdateUser(Job job, User user) {
        if (Objects.nonNull(user)) {
            job.setUpdatedUserId(user.getUserId());
            job.setUpdatedUserName(user.getUserName());
            job.setUpdatedUserPhone(user.getPhone());
        }
    }

    /**
     * 列表查询
     * @param jobQuery jobQuery
     * @return PageInfo<Job>
     */
    public PageInfo<JobListDTO> list(JobQuery jobQuery) {
        JobCondition condition = new JobCondition();
        JobCondition.Criteria criteria = condition.createCriteria();
        // 标题
        if (StringUtils.isNotBlank(jobQuery.getJobTitle())) {
            criteria.andJobTitleLike("%" + jobQuery.getJobTitle() + "%");
        }
        // 创建人
        if (StringUtils.isNotBlank(jobQuery.getCreatedUserId())) {
            criteria.andCreatedUserIdEqualTo(jobQuery.getCreatedUserId());
        }
        // 创建人手机号
        if (StringUtils.isNotBlank(jobQuery.getCreatedUserPhone())) {
            criteria.andCreatedUserPhoneEqualTo(jobQuery.getCreatedUserPhone());
        }
        // 任务类型
        if (StringUtils.isNotBlank(jobQuery.getJobTypeId())) {
            criteria.andJobTypeIdEqualTo(jobQuery.getJobTypeId());
        }
        // 状态
        if (StringUtils.isNotBlank(jobQuery.getStatus())) {
            criteria.andStatusEqualTo(jobQuery.getStatus());
        }
        // 是否删除
        if (Objects.nonNull(jobQuery.getDeleted())) {
            criteria.andDeletedEqualTo(jobQuery.getDeleted());
        }

        if (Objects.nonNull(jobQuery.getExecuteStatus())) {
            criteria.andExecuteStatusEqualTo(jobQuery.getExecuteStatus());
        }

        if (Objects.nonNull(jobQuery.getOrderBy())) {
            condition.setOrderByClause(jobQuery.getOrderBy());
        } else {
            condition.setOrderByClause("created_time desc");
        }

        PageHelper.startPage(jobQuery.getPage(), jobQuery.getSize());

        List<Job> jobList = iJobMapper.selectByExample(condition);
        PageInfo pageInfo = new PageInfo<>(jobList);
        List<JobListDTO> jobListDTOList = new ArrayList<>();
        BeanCopier beanCopier = BeanCopier.create(Job.class, JobListDTO.class, false);
        for (Job job : jobList) {
            JobListDTO jobListDTO = new JobListDTO();
            beanCopier.copy(job, jobListDTO, null);
            List<JobCenterFile> jobCenterFileList = fileService.list(job.getJobId(), ObjectTypeEnum.JOB, FileTypeEnum.DOWNLOAD_DATA_FILE);
            if (!CollectionUtils.isEmpty(jobCenterFileList)) {
                jobListDTO.setFileCdn(jobCenterFileList.get(0).getFileCdn());
            }
            jobListDTOList.add(jobListDTO);
        }
        pageInfo.setList(jobListDTOList);
        return pageInfo;
    }

    /**
     * 根据JobId获取详情
     * @param jobId jobId
     * @return Job
     */
    public Job get(String jobId) {
        return iJobMapper.selectByJobId(jobId);
    }

    /**
     * 获取待执行的任务
     * @return PageInfo<Job>
     */
    public PageInfo<Job> waitExecuteJobList() {

        JobCondition condition = new JobCondition();
        condition.createCriteria()
                .andStatusEqualTo(StatusEnum.COMMON_AUDITED.getStatus())
                .andExecuteStatusEqualTo(StatusEnum.JOB_EXECUTE_STATUS_WAIT.getStatus())
                .andDeletedEqualTo(GlobalEnum.NO.getValue());

        PageHelper.startPage(1, 10);
        List<Job> jobList = iJobMapper.selectByExample(condition);

        return new PageInfo<>(jobList);
    }

    /**
     * 获取执行中的任务
     * @return PageInfo<Job>
     */
    public PageInfo<Job> runningOverTimeExecuteJobList() {

        JobCondition condition = new JobCondition();
        condition.createCriteria()
                .andStatusEqualTo(StatusEnum.COMMON_AUDITED.getStatus())
                .andExecuteStatusEqualTo(StatusEnum.JOB_EXECUTE_STATUS_RUNNING.getStatus())
                .andUpdatedTimeLessThan(Instant.now().minus(1, ChronoUnit.HOURS))
                .andDeletedEqualTo(GlobalEnum.NO.getValue());

        PageHelper.startPage(1, 10);
        List<Job> jobList = iJobMapper.selectByExample(condition);

        return new PageInfo<>(jobList);
    }

    /**
     * 根据任务ID和任务执行状态更新任务执行状态
     * @param job 任务
     * @param executeStatus 任务执行状态
     */
    public void updateExecuteStatus(Job job, StatusEnum executeStatus) {
        Job updateJob = new Job();
        updateJob.setExecuteStatus(executeStatus.getStatus());

        JobCondition updateCondition = new JobCondition();
        updateCondition.createCriteria()
                .andJobIdEqualTo(job.getJobId());

        iJobMapper.updateByExampleSelective(updateJob, updateCondition);
        log.info("根据任务ID和任务执行状态更新任务执行状态, job:{},updated ExecuteStatus:{}", job, executeStatus);
    }

}
