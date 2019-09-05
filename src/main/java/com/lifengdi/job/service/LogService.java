package com.lifengdi.job.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lifengdi.enums.OperateTypeEnum;
import com.lifengdi.job.entity.JobLog;
import com.lifengdi.job.entity.JobTypeLog;
import com.lifengdi.job.example.JobLogCondition;
import com.lifengdi.job.mapper.JobLogMapper;
import com.lifengdi.job.mapper.JobTypeLogMapper;
import com.lifengdi.model.User;
import com.lifengdi.model.query.JobLogQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author 李锋镝
 * @date Create at 18:44 2019/4/2
 */
@Service
@Slf4j
public class LogService {

    @Resource
    private JobTypeLogMapper jobTypeLogMapper;

    @Resource
    private JobLogMapper jobLogMapper;

    /**
     * 插入JobType的操作日志
     * @param jobTypeId jobTypeId
     * @param operateTypeEnum operateTypeEnum
     * @param oldContent oldContent
     * @param newContent newContent
     * @param operateDesc operateDesc(可以为空)
     * @param user user
     */
    @Async
    public void addJobTypeLog(String jobTypeId, OperateTypeEnum operateTypeEnum, String oldContent, String newContent, String operateDesc, User user) {
        JobTypeLog jobTypeLog = buildJobTypeLog(jobTypeId, operateTypeEnum, oldContent, newContent, operateDesc, user);
        jobTypeLogMapper.insertSelective(jobTypeLog);
        log.info("新增JobType的操作日志，jobTypeLog：{}", JSON.toJSONString(jobTypeLog));
    }

    private JobTypeLog buildJobTypeLog(String jobTypeId, OperateTypeEnum operateTypeEnum, String oldContent, String newContent, String operateDesc, User user) {
        JobTypeLog jobTypeLog = new JobTypeLog();
        jobTypeLog.setJobTypeId(jobTypeId);
        jobTypeLog.setOperateType(operateTypeEnum.getOperateType());
        String opDesc = StringUtils.isBlank(operateDesc) ? operateTypeEnum.getOperateDesc() : operateTypeEnum.getOperateDesc() + ":" + operateDesc;
        jobTypeLog.setOperateDesc(opDesc);
        jobTypeLog.setOperatorId(user.getUserId());
        jobTypeLog.setOperatorName(user.getUserName());
        jobTypeLog.setOldContent(oldContent);
        jobTypeLog.setNewContent(newContent);
        return jobTypeLog;
    }

    private JobLog buildJobLog(String jobId, String jobTypeId, OperateTypeEnum operateTypeEnum, String content, String result, User user) {
        JobLog jobLog = new JobLog();
        jobLog.setJobId(jobId);
        jobLog.setJobTypeId(jobTypeId);
        jobLog.setOperateContent(content);
        jobLog.setOperateResult(result);
        jobLog.setOperateType(operateTypeEnum.getOperateType());
        if (Objects.nonNull(user)) {
            jobLog.setOperatorId(user.getUserId());
            jobLog.setOperatorName(user.getUserName());
            jobLog.setOperatorPhone(user.getPhone());
        }
        return jobLog;
    }

    /**
     * 任务的操作日志
     * @param jobId 任务ID
     * @param jobTypeId 任务类型ID
     * @param operateTypeEnum 操作类型
     * @param content 内容
     * @param result 结果
     * @param user 操作用户
     */
    @Async
    public void addJobLog(String jobId, String jobTypeId, OperateTypeEnum operateTypeEnum, String content, String result, User user) {
        JobLog jobLog = buildJobLog(jobId, jobTypeId, operateTypeEnum, content, result, user);
        jobLogMapper.insertSelective(jobLog);
        log.info("Job的操作日志，jobLog：{}", JSON.toJSONString(jobLog));
    }

    /**
     * 列表查询
     * @param jobLogQuery JobLogQuery
     * @return PageInfo<JobLog>
     */
    public PageInfo<JobLog> listJobLog(JobLogQuery jobLogQuery) {
        JobLogCondition condition = new JobLogCondition();
        JobLogCondition.Criteria criteria = condition.createCriteria();
        if (StringUtils.isNotBlank(jobLogQuery.getOperatorId())) {
            criteria.andOperatorIdEqualTo(jobLogQuery.getOperatorId());
        }
        if (StringUtils.isNotBlank(jobLogQuery.getOperatorPhone())) {
            criteria.andOperatorPhoneEqualTo(jobLogQuery.getOperatorPhone());
        }
        if (StringUtils.isNotBlank(jobLogQuery.getOperateType())) {
            criteria.andOperateTypeEqualTo(jobLogQuery.getOperateType());
        }
        if (StringUtils.isNotBlank(jobLogQuery.getJobId())) {
            criteria.andJobIdEqualTo(jobLogQuery.getJobId());
        }
        if (StringUtils.isNotBlank(jobLogQuery.getJobTypeId())) {
            criteria.andJobTypeIdEqualTo(jobLogQuery.getJobTypeId());
        }

        if (Objects.nonNull(jobLogQuery.getOrderBy())) {
            condition.setOrderByClause(jobLogQuery.getOrderBy());
        } else {
            condition.setOrderByClause("created_time desc");
        }

        PageHelper.startPage(jobLogQuery.getPage(), jobLogQuery.getSize());
        List<JobLog> jobLogList = jobLogMapper.selectByExample(condition);
        return new PageInfo<>(jobLogList);
    }

    /**
     * 列表查询
     * @param id id
     * @return JobLog
     */
    public JobLog detailJobLog(Long id) {

        return jobLogMapper.selectByPrimaryKey(id);
    }
}
