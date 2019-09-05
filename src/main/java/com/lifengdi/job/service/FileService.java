package com.lifengdi.job.service;

import com.lifengdi.config.QCloudCOSConfiguration;
import com.lifengdi.enums.*;
import com.lifengdi.exception.AppException;
import com.lifengdi.job.entity.JobCenterFile;
import com.lifengdi.job.example.JobCenterFileCondition;
import com.lifengdi.job.mapper.JobCenterFileMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 文件
 * @author 李锋镝
 * @date Create at 14:26 2019/4/3
 */
@Service
public class FileService {

    @Resource
    private JobCenterFileMapper jobCenterFileMapper;

    @Resource
    private QCloudCOSConfiguration qCloudCOSConfiguration;

    /**
     * 新增file
     * @param file File
     * @param objId objId
     */
    public JobCenterFile insert(JobCenterFile file, String objId, ObjectTypeEnum objectTypeEnum) {

        Objects.requireNonNull(file);
        Objects.requireNonNull(file.getFileUrl());
        Objects.requireNonNull(file.getFileCdn());
        Objects.requireNonNull(objId);

        if (!FileTypeEnum.judgeFileSource(objectTypeEnum)) {
            throw AppException.FILE_TYPE_ADD_SOURCE_ERROR.build();
        }

        if (Objects.isNull(file.getFileAnalysisModel())) {
            throw AppException.ADD_FILE_NO_ANALYSIS_MODEL_ERROR.build();
        }

        if (!FileAnalysisModelEnum.judgeFileAnalysisModel(file.getFileAnalysisModel())) {
            throw  AppException.FILE_ANALYSIS_MODEL_NOT_SUPPORT.builds(file.getFileAnalysisModel());
        }

        file.setObjId(objId);
        file.setObjType(objectTypeEnum.getType());
        file.setDeleted(GlobalEnum.NO.getValue());
        file.setStatus(StatusEnum.COMMON_AUDITED.getStatus());

        jobCenterFileMapper.insertSelective(file);

        return file;
    }

    /**
     * 批量删除
     * @param objId objId
     * @param objectTypeEnum see {@linkplain ObjectTypeEnum}
     */
    public void batchDelete(String objId, ObjectTypeEnum objectTypeEnum) {
        Objects.requireNonNull(objId);
        Objects.requireNonNull(objectTypeEnum);

        JobCenterFileCondition condition = new JobCenterFileCondition();
        condition.createCriteria().andObjIdEqualTo(objId).andObjTypeEqualTo(objectTypeEnum.getType());

        JobCenterFile file = new JobCenterFile();
        file.setDeleted(GlobalEnum.YES.getValue());

        jobCenterFileMapper.updateByExampleSelective(file, condition);
    }

    /**
     * 根据对象ID和对象类型查询
     *
     * @param objId          objId
     * @param objectTypeEnum see {@linkplain ObjectTypeEnum}
     * @param fileTypeEnum see {@linkplain FileTypeEnum}
     */
    public List<JobCenterFile> list(String objId, ObjectTypeEnum objectTypeEnum, FileTypeEnum fileTypeEnum) {
        Objects.requireNonNull(objId);
        Objects.requireNonNull(objectTypeEnum);

        JobCenterFileCondition condition = new JobCenterFileCondition();
        condition.createCriteria()
                .andObjIdEqualTo(objId)
                .andObjTypeEqualTo(objectTypeEnum.getType())
                .andDeletedEqualTo(GlobalEnum.NO.getValue())
                .andFileTypeEqualTo(fileTypeEnum.getFileType());

        return jobCenterFileMapper.selectByExample(condition);
    }

    /**
     * 根据对象ID和对象类型查询(文件加载有临时访问地址)
     *
     * @param objId          objId
     * @param objectTypeEnum see {@linkplain ObjectTypeEnum}
     */
    public List<JobCenterFile> listWithTempUrl(String objId, ObjectTypeEnum objectTypeEnum) {

        List<JobCenterFile> list = list(objId, objectTypeEnum);
        if (!CollectionUtils.isEmpty(list)) {
            for (JobCenterFile jobCenterFile : list) {
                String cacheUrl = qCloudCOSConfiguration.fileCacheUrl(qCloudCOSConfiguration.qCloudUrl(jobCenterFile.getFileCdn()));
                jobCenterFile.setFileTempUrl(cacheUrl);
                jobCenterFile.setFileUrl(cacheUrl);
            }
        }
        return list;
    }

    /**
     * 根据对象ID和对象类型查询
     *
     * @param objId          objId
     * @param objectTypeEnum see {@linkplain ObjectTypeEnum}
     */
    public List<JobCenterFile> list(String objId, ObjectTypeEnum objectTypeEnum) {
        Objects.requireNonNull(objId);
        Objects.requireNonNull(objectTypeEnum);

        JobCenterFileCondition condition = new JobCenterFileCondition();
        condition.createCriteria()
                .andObjIdEqualTo(objId)
                .andObjTypeEqualTo(objectTypeEnum.getType())
                .andDeletedEqualTo(GlobalEnum.NO.getValue());

        return jobCenterFileMapper.selectByExample(condition);
    }

}
