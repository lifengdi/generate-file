package com.lifengdi.job.service.business.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lifengdi.enums.FileTypeEnum;
import com.lifengdi.enums.GlobalEnum;
import com.lifengdi.enums.ObjectTypeEnum;
import com.lifengdi.enums.OperateTypeEnum;
import com.lifengdi.exception.AppException;
import com.lifengdi.job.entity.JobCenterFile;
import com.lifengdi.job.entity.JobType;
import com.lifengdi.job.entity.RequestParam;
import com.lifengdi.job.service.FileService;
import com.lifengdi.job.service.JobTypeService;
import com.lifengdi.job.service.LogService;
import com.lifengdi.job.service.RequestParamService;
import com.lifengdi.job.service.business.JobTypeBusinessService;
import com.lifengdi.model.User;
import com.lifengdi.model.dto.JobTypeDTO;
import com.lifengdi.model.query.JobTypeQuery;
import com.lifengdi.util.GeneratedKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 李锋镝
 * @date Create at 16:40 2019/4/2
 */
@Service
@Slf4j
public class JobTypeBusinessServiceImpl implements JobTypeBusinessService {

    @Resource
    private JobTypeService jobTypeService;

    @Resource
    private GeneratedKey generatedKey;

    @Resource
    private RequestParamService requestParamService;

    @Resource
    private LogService logService;

    @Resource
    private FileService fileService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(JobTypeDTO jobTypeDTO, User user) {
        JobType jobType = jobTypeDTO.getJobType();
        List<RequestParam> requestParamList = jobTypeDTO.getRequestParamList();
        List<JobCenterFile> fileList = jobTypeDTO.getFileList();

        checkFile(jobType, requestParamList, fileList);

        final String jobTypeId = generatedKey.generatorKey();
        log.info("新增任务类型，生成jobTypeId为:{}，用户ID:{}", jobTypeId, user.getUserId());
        jobType.setJobTypeId(jobTypeId);

        // 新增任务类型
        jobTypeService.insert(jobType, user);
        log.info("新增任务类型，jobType：{}", JSON.toJSONString(jobType));

        // 新增请求参数
        if (!CollectionUtils.isEmpty(requestParamList)) {
            requestParamList.forEach(requestParam -> {
                requestParamService.insertOrUpdate(requestParam, user, jobTypeId, ObjectTypeEnum.JOB_TYPE);
                log.info("新增任务类型-新增请求参数，requestParam：{}", JSON.toJSONString(requestParam));
            });
        }

        // 新增模板文件
        if (!CollectionUtils.isEmpty(fileList)) {
            fileList.forEach(file -> {
                fileService.insert(file, jobTypeId, ObjectTypeEnum.JOB_TYPE);
                log.info("新增任务类型-新增模板文件，file：{}", JSON.toJSONString(file));
            });
        }

        logService.addJobTypeLog(jobTypeId, OperateTypeEnum.ADD_JOB_TYPE, null, JSON.toJSONString(jobTypeDTO), null, user);
    }

    private void checkFile(JobType jobType, List<RequestParam> requestParamList, List<JobCenterFile> fileList) {
        boolean hasUploadFileTemp = false;
        boolean hasDownloadFileTemp = false;
        if (!CollectionUtils.isEmpty(fileList)) {
            for (JobCenterFile file : fileList) {
                if (file.getFileType().equals(FileTypeEnum.UPLOAD_FILE_TEMP.getFileType())) {
                    hasUploadFileTemp = true;
                    continue;
                }
                if (hasUploadFileTemp) {
                    throw AppException.TEMPLATE_FILE_TOO_MORE.builds(FileTypeEnum.UPLOAD_FILE_TEMP.getTypeDesc());
                }
                if (file.getFileType().equals(FileTypeEnum.DOWNLOAD_FILE_TEMP.getFileType())) {
                    hasDownloadFileTemp = true;
                    continue;
                }
                if (hasDownloadFileTemp) {
                    throw AppException.TEMPLATE_FILE_TOO_MORE.builds(FileTypeEnum.DOWNLOAD_FILE_TEMP.getTypeDesc());
                }
            }
        }
        if (jobType.getGenerateFile().equals(GlobalEnum.YES.getValue()) && !hasDownloadFileTemp) {
            throw AppException.NEED_UPLOAD_GENERATE_TEMPLATE_FILE.build();
        }

        if (jobType.getUploadFile().equals(GlobalEnum.YES.getValue())) {
            if (!hasUploadFileTemp && CollectionUtils.isEmpty(requestParamList)) {
                // 需要上传文件的任务类型，请求参数和上传文件的解析模板二选一必传
                throw AppException.JOB_TYPE_NEED_UPLOAD_FILE_NO_PARAM_AND_TEMPLATE_FILE.build();
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(JobTypeDTO jobTypeDTO, User user) {

        JobType jobType = jobTypeDTO.getJobType();
        String jobTypeId = jobType.getJobTypeId();
        if (StringUtils.isEmpty(jobTypeId)) {
            throw AppException.JOB_TYPE_NOT_FOUND_ID.build();
        }
        List<RequestParam> requestParamList = jobTypeDTO.getRequestParamList();
        List<JobCenterFile> fileList = jobTypeDTO.getFileList();

        checkFile(jobType, requestParamList, fileList);

        requestParamService.batchDelete(jobTypeId, ObjectTypeEnum.JOB_TYPE, user);
        log.info("批量删除RequestParam， jobTypeId:{}", jobTypeId);
        fileService.batchDelete(jobTypeId, ObjectTypeEnum.JOB_TYPE);
        log.info("批量删除File， jobTypeId:{}", jobTypeId);

        jobTypeService.update(jobType, user);
        log.info("修改任务类型，jobTypeId为:{}，用户登录号:{}", jobTypeId, user.getUserLoginName());

        if (!CollectionUtils.isEmpty(requestParamList)) {
            requestParamList.forEach(requestParam -> {
                requestParamService.insertOrUpdate(requestParam, user, jobTypeId, ObjectTypeEnum.JOB_TYPE);
                log.info("修改任务类型-新增/修改请求参数，requestParam：{}", JSON.toJSONString(requestParam));
            });
        }

        // 新增模板文件
        if (!CollectionUtils.isEmpty(fileList)) {
            fileList.forEach(file -> {
                fileService.insert(file, jobTypeId, ObjectTypeEnum.JOB_TYPE);
                log.info("修改任务类型-新增模板文件，file：{}", JSON.toJSONString(file));
            });
        }
    }

    @Override
    public PageInfo<JobType> list(JobTypeQuery jobTypeQuery) {

        return jobTypeService.list(jobTypeQuery);
    }

    @Override
    public JobType detail(String jobTypeId) {
        return jobTypeService.get(jobTypeId);
    }

    @Override
    public void delete(String jobTypeId, User user) {
        jobTypeService.delete(jobTypeId, user);
    }
}
