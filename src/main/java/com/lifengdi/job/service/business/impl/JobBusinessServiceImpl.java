package com.lifengdi.job.service.business.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lifengdi.enums.*;
import com.lifengdi.exception.AppException;
import com.lifengdi.job.entity.Job;
import com.lifengdi.job.entity.JobCenterFile;
import com.lifengdi.job.entity.JobType;
import com.lifengdi.job.entity.RequestParam;
import com.lifengdi.job.execute.ExecuteJob;
import com.lifengdi.job.service.*;
import com.lifengdi.job.service.business.JobBusinessService;
import com.lifengdi.model.User;
import com.lifengdi.model.dto.JobDTO;
import com.lifengdi.model.dto.JobListDTO;
import com.lifengdi.model.query.JobQuery;
import com.lifengdi.util.GeneratedKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author 李锋镝
 * @date Create at 14:21 2019/4/4
 */
@Service
@Slf4j
public class JobBusinessServiceImpl implements JobBusinessService {

    @Resource
    private JobService jobService;

    @Resource
    private GeneratedKey generatedKey;

    @Resource
    private RequestParamService requestParamService;

    @Resource
    private FileService fileService;

    @Resource
    private JobTypeService jobTypeService;

    @Resource
    private LogService logService;

    @Resource
    private ExecuteJob executeJob;

    @Value("${spring.jackson.date-format}")
    private String DATE_FORMAT;

    private static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(5);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(JobDTO jobDTO, User user) {

        Job job = jobDTO.getJob();
        List<RequestParam> requestParamList = jobDTO.getRequestParamList();
        List<JobCenterFile> fileList = jobDTO.getFileList();

        String jobTypeId = job.getJobTypeId();
        if (StringUtils.isBlank(jobTypeId)) {
            throw AppException.JOB_WITHOUT_JOB_TYPE.build();
        }
        JobType jobType = jobTypeService.get(jobTypeId);
        if (Objects.isNull(jobType)) {
            throw AppException.ADD_JOB_NOT_FOUND_JOB_TYPE.build();
        }
        if (jobType.getDeleted().equals(GlobalEnum.YES.getValue())) {
            throw AppException.ADD_JOB_FOUND_JOB_TYPE_INVALID.build();
        }
        checkAndSetJobParam(requestParamList, jobTypeId);
        checkAndSetJobFile(fileList, jobType);

        String jobId = generatedKey.generatorKey();
        job.setJobId(jobId);
        jobService.insert(job, user);

        // 新增请求参数
        if (!CollectionUtils.isEmpty(requestParamList)) {
            requestParamList.forEach(requestParam -> {
                if (StringUtils.isNotBlank(requestParam.getParamValue())) {
                    requestParamService.insertOrUpdate(requestParam, user, jobId, ObjectTypeEnum.JOB);
                    log.info("新增JOB-新增请求参数，requestParam:{}", JSON.toJSONString(requestParam));
                }
            });
        }

        // 新增模板文件
        if (!CollectionUtils.isEmpty(fileList)) {
            fileList.forEach(file -> {
                file.setFileType(FileTypeEnum.UPLOAD_DATA_FILE.getFileType());
                fileService.insert(file, jobId, ObjectTypeEnum.JOB);
                log.info("新增JOB-新增模板文件，file:{}", JSON.toJSONString(file));
            });
        }

        logService.addJobLog(jobId, jobTypeId, OperateTypeEnum.ADD_JOB, OperateTypeEnum.ADD_JOB.getOperateDesc(), OperateTypeEnum.ADD_JOB.getSuccess(), user);
    }

    /**
     * 根据任务类型判断任务参数的有效性
     * @param requestParamList requestParamList
     * @param jobTypeId jobTypeId
     */
    private void checkAndSetJobParam(List<RequestParam> requestParamList, String jobTypeId) {
        // 判断入参有效性
        List<RequestParam> jobTypeRequestParamList = requestParamService.list(jobTypeId, ObjectTypeEnum.JOB_TYPE);
        if (!CollectionUtils.isEmpty(jobTypeRequestParamList)) {
            List<String> requestParamNameList = new ArrayList<>();
            List<String> jobTypeRequestParamNameList = jobTypeRequestParamList.stream().map(RequestParam::getParamName).distinct().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(requestParamList)) {
                requestParamNameList = requestParamList.stream().map(RequestParam::getParamName).distinct().collect(Collectors.toList());
                if (requestParamList.size() != requestParamNameList.size()) {
                    // 是否有重复参数
                    throw AppException.PARAM_REPEAT.build();
                }
            }

            requestParamList.removeIf(requestParam -> !jobTypeRequestParamNameList.contains(requestParam.getParamName()));

            for (RequestParam jobTypeRequestParam : jobTypeRequestParamList) {
                String jobTypeParamName = jobTypeRequestParam.getParamName();
                String jobTypeParamType = jobTypeRequestParam.getParamType();
                String jobTypeFormat = jobTypeRequestParam.getParamFormat();
                // 校验必传参数
                {
                    if (jobTypeRequestParam.getRequired() == GlobalEnum.YES.getValue()) {
                        if (CollectionUtils.isEmpty(requestParamList)) {
                            throw AppException.REQUIRED_PARAM_CAN_NOT_BE_NULL.builds(jobTypeRequestParam.getParamTitle());
                        }
                        if (!requestParamNameList.contains(jobTypeParamName)) {
                            throw AppException.REQUIRED_PARAM_CAN_NOT_BE_NULL.builds(jobTypeRequestParam.getParamTitle());
                        }
                    }
                }
                // 入参有任务类型中指定的参数 则判断参数类型
                {
                    if (requestParamNameList.contains(jobTypeParamName)) {
                        for (RequestParam requestParam : requestParamList) {
                            if (jobTypeParamName.equals(requestParam.getParamName())) {
                                String paramValue = requestParam.getParamValue();
                                if (StringUtils.isNotBlank(paramValue)) {
                                    ParamTypeEnum paramTypeEnum = ParamTypeEnum.get(jobTypeParamType);
                                    if (ParamTypeEnum.ARRAY_DATETIME.equals(paramTypeEnum) || ParamTypeEnum.DATETIME.equals(paramTypeEnum)) {
                                        // 如果是时间日期类型的 参数格式为空 则按照默认格式来校验
                                        if (StringUtils.isBlank(jobTypeFormat)) {
                                            jobTypeFormat = DATE_FORMAT;
                                        }
                                    }
                                    if (!ParamTypeEnum.judgeParamFormat(jobTypeParamType, paramValue, jobTypeFormat)) {
                                        throw AppException.PARAM_FORMAT_ILLEGAL.builds(jobTypeParamName, jobTypeParamType);
                                    }
                                    requestParam.setParamType(jobTypeParamType);
                                    requestParam.setParamFormat(jobTypeFormat);
                                    requestParam.setParamTitle(jobTypeRequestParam.getParamTitle());
                                    requestParam.setRequired(jobTypeRequestParam.getRequired());
                                }

                            }
                        }
                    }
                }
            }
        } else {
            if (!CollectionUtils.isEmpty(requestParamList)) {
                requestParamList.clear();
            }
        }
    }

    /**
     * 根据任务类型判断上传文件的有效性
     * @param fileList fileList
     * @param jobType jobType
     */
    private void checkAndSetJobFile(List<JobCenterFile> fileList, JobType jobType) {
        if (!CollectionUtils.isEmpty(fileList)) {
            if (jobType.getUploadFile() == GlobalEnum.NO.getValue()) {
                throw AppException.ILLEGAL_FILE_UPLOAD.build();
            }
            List<JobCenterFile> jobTypeFileList = fileService.list(jobType.getJobTypeId(), ObjectTypeEnum.JOB_TYPE, FileTypeEnum.UPLOAD_FILE_TEMP);
            fileList.forEach(file -> {
                if (!FileTypeEnum.judgeFileSource(ObjectTypeEnum.JOB)) {
                    throw AppException.FILE_TYPE_ADD_SOURCE_ERROR.build();
                }
                if (StringUtils.isBlank(file.getFileUrl())) {
                    throw AppException.FILE_WITHOUT_URL.build();
                }
                // 设置文件解析模式
                if (CollectionUtils.isEmpty(jobTypeFileList)) {
                    // 任务类型需要上传文件，但是没有对应的模板，则使用循序解析
                    file.setFileAnalysisModel(FileAnalysisModelEnum.IN_TURN.getModel());
                } else {
                    file.setFileAnalysisModel(jobTypeFileList.get(0).getFileAnalysisModel());
                }
            });
        } else {
            // 需要上传文件
            if (jobType.getUploadFile() == GlobalEnum.YES.getValue()) {
                throw AppException.FILE_WITHOUT_URL.build();
            }
        }
    }

    @Override
    public PageInfo<JobListDTO> list(JobQuery jobQuery) {
        return jobService.list(jobQuery);
    }

    @Override
    public Job get(String jobId) {
        return jobService.get(jobId);
    }

    @Override
    public void executeJob() {
        overtime();
        run();
    }

    /**
     * 执行任务
     */
    private void run() {
        log.info("执行任务");
        PageInfo<Job> jobPageInfo = jobService.waitExecuteJobList();
        List<Job> jobList = jobPageInfo.getList();
        log.info("job size is :{}",jobList == null?0:jobList.size());
        while (!CollectionUtils.isEmpty(jobList)) {
            if (CollectionUtils.isEmpty(jobList)) {
                break;
            }
            for (Job job : jobList) {
                log.info("准备执行任务,JOB:{}", job);
                EXECUTORS.execute(() -> executeJob.execute(job));
            }
            try {
                // 休眠十秒
                Thread.sleep(1000L * 10);
            } catch (InterruptedException e) {
                log.error("线程休眠异常", e);
            }
            jobList = jobService.waitExecuteJobList().getList();
        }
    }

    /**
     * 超时处理
     */
    private void overtime() {
        PageInfo<Job> runningJobPageInfo = jobService.runningOverTimeExecuteJobList();
        List<Job> runningJobList = runningJobPageInfo.getList();
        log.info("running job size is :",runningJobList==null?0:runningJobList.size());
        while (!CollectionUtils.isEmpty(runningJobList)) {
            if (CollectionUtils.isEmpty(runningJobList)) {
                break;
            }
            for (Job job : runningJobList) {
                // 任务更新时间是一个小时之前 && 任务状态是执行中
                if (job.getUpdatedTime().isBefore(Instant.now().minus(1, ChronoUnit.HOURS))
                        && StatusEnum.JOB_EXECUTE_STATUS_RUNNING.getStatus().equals(job.getExecuteStatus())) {
                    // 认为任务执行失败
                    log.info("任务 {} 执行超时，jobId:{}", job.getJobTitle(), job.getJobId());
                    jobService.updateExecuteStatus(job, StatusEnum.JOB_EXECUTE_STATUS_FAILED);
                    // 添加日志
                    logService.addJobLog(job.getJobId(), job.getJobTypeId(), OperateTypeEnum.EXECUTE_JOB, OperateTypeEnum.EXECUTE_JOB.getOperateDesc() + "-超时", OperateTypeEnum.EXECUTE_JOB.getFailed(), null);
                }
            }
            runningJobList = jobService.runningOverTimeExecuteJobList().getList();
        }
    }
}
