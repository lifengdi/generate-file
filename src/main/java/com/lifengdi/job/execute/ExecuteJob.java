package com.lifengdi.job.execute;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.lifengdi.commons.utils.Either;
import com.lifengdi.config.QCloudCOSConfiguration;
import com.lifengdi.config.SuccessConfig;
import com.lifengdi.constant.LockKey;
import com.lifengdi.constant.ThreadLocalCache;
import com.lifengdi.enums.*;
import com.lifengdi.exception.ApiException;
import com.lifengdi.file.excel.impl.ExcelAnalysisModelImpl;
import com.lifengdi.http.HttpUtil;
import com.lifengdi.job.entity.Job;
import com.lifengdi.job.entity.JobCenterFile;
import com.lifengdi.job.entity.JobType;
import com.lifengdi.job.entity.RequestParam;
import com.lifengdi.job.service.*;
import com.lifengdi.model.ParamBO;
import com.lifengdi.model.vo.ZipVo;
import com.lifengdi.response.ResponseResult;
import com.lifengdi.util.DistributedLocks;
import com.lifengdi.util.FileUtil;
import com.lifengdi.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author 李锋镝
 * @date Create at 14:43 2019/4/10
 */
@Component
@Slf4j
public class ExecuteJob {

    @Resource
    private DistributedLocks distributedLocks;

    @Resource
    private JobTypeService jobTypeService;

    @Resource
    private RequestParamService requestParamService;

    @Resource
    private ExcelAnalysisModelImpl excelAnalysisModelImpl;

    @Resource
    private FileService fileService;

    @Resource
    private QCloudCOSConfiguration qCloudCOSConfiguration;

    @Resource
    private HttpUtil httpUtil;

    @Resource
    private JobService jobService;

    @Resource
    private LogService logService;

    @Resource
    private SuccessConfig successConfig;

    @Resource
    private FileUtil fileUtil;


    @Value("${file.localTempPath}")
    private String localTempPath;

    /**
     * 执行任务
     * @param job job
     */
    public void execute(Job job) {
        log.info("execute job :{}",job.getJobId());
        if (Objects.isNull(job)) {
            return;
        }
        String jobId = job.getJobId();
        String jobTypeId = job.getJobTypeId();
        String key = LockKey.getKeyByFormat(jobId, LockKey.JOB_CENTER_EXECUTE_JOB_ID);
        log.info("execute job key :{}",key);

        if (distributedLocks.sixtyMinutesLock(key)) {
            log.info("获取锁成功，key:{},jobId:{}", key, jobId);
            ThreadLocalCache.RESULT_THREAD_LOCAL.remove();
            ThreadLocalCache.RESULT_ERROR_INFO_THREAD_LOCAL.remove();
            job = jobService.get(jobId);
            if (!StatusEnum.JOB_EXECUTE_STATUS_WAIT.getStatus().equals(job.getExecuteStatus())) {
                log.info("任务状态不是待执行状态，跳过不执行，jobId:{},ExecuteStatus:{}", jobId, job.getExecuteStatus());
                return;
            }
            StatusEnum executeStatus = StatusEnum.JOB_EXECUTE_STATUS_SUCCESS;
            String cacheUrl = null;
            boolean success = false, exception = false;
            try {
                JobType jobType = jobTypeService.get(jobTypeId);
                if (Objects.nonNull(jobType)) {
                    // 更改任务执行状态为执行中
                    jobService.updateExecuteStatus(job, StatusEnum.JOB_EXECUTE_STATUS_RUNNING);
                    logService.addJobLog(jobId, jobTypeId, OperateTypeEnum.EXECUTE_JOB, OperateTypeEnum.EXECUTE_JOB.getOperateDesc(), OperateTypeEnum.EXECUTE_JOB.getBegin(), null);
                    List<RequestParam> jobTypeParamList = requestParamService.list(jobTypeId, ObjectTypeEnum.JOB_TYPE);
                    List<RequestParam> jobParamList = requestParamService.list(jobId, ObjectTypeEnum.JOB);
                    // 任务类型是否需要上传文件
                    boolean needUploadFile = GlobalEnum.YES.getValue() == jobType.getUploadFile();
                    // 任务类型是否需要生成文件（什么类型的文件）
                    boolean needDownloadFile = GlobalEnum.YES.getValue() == jobType.getGenerateFile();

                    ResponseDataEntityEnum entityEnum = ResponseDataEntityEnum.get(jobType.getResponseDataEntity());
                    String requestUrl = jobType.getRequestUrl();
                    RequestMethodEnum requestMethod = RequestMethodEnum.get(jobType.getRequestMethod());

                    List<Object> writeData = new ArrayList();// 要生成Excel的数据
                    if (FileFormatEnum.CREATE_PDF_FILE.getKey() == jobType.getGenerateFile()) {
                        //压缩文件处理
                        //获取所有的临时下载url
                        if (CollectionUtils.isEmpty(jobParamList)) {
                            return;
                        }
                        Map<String, Object> params = new HashMap<>();
                        for (RequestParam requestParam : jobParamList) {
                            params.put(requestParam.getParamName(), ParamTypeEnum.formatParamValue(requestParam));
                        }
                        Either<ApiException, ResponseResult> responseResultEither = _request(false, entityEnum, requestUrl, requestMethod, null, params);

                        Map<String, String> successCode = successConfig.getSuccessCode();
                        if (responseResultEither.isLeft() || !successCode.containsKey(responseResultEither.getRight().getCode())) {
                            log.error("请求接口出现异常，requestUrl:{},params:{}", requestUrl, params);
                            return;
                        }

                        List<Map> list = (List<Map>)responseResultEither.getRight().getData();
                        if (CollectionUtils.isEmpty(list)) {
                            return;
                        }
                        List<ZipVo> zipVos = new ArrayList<>();
                        for (Map map : list) {
                            zipVos.add(ObjectUtils.mapToObject(map, new ZipVo()));
                        }
                        //获取zip url
                        cacheUrl = getZipUrl(zipVos,job);
                        if (StringUtils.isNotBlank(cacheUrl)) {
                            success = true;
                        }
                    } else {
                        if (needUploadFile) {
                            log.info("uploadFile,jobId:{}", jobId);
                            success = uploadFile(jobId, jobTypeId, jobTypeParamList, needDownloadFile, entityEnum, requestUrl, requestMethod, writeData);
                        } else {
                            log.info("unUploadFile,jobId:{}", jobId);
                            // 无需上传文件，直接根据参数和值执行任务
                            success = unUploadFile(jobParamList, needDownloadFile, entityEnum, requestUrl, requestMethod, writeData);
                        }
                    }

                    if (needDownloadFile && !CollectionUtils.isEmpty(writeData) && success) {
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        String fileName = job.getId() + "-" + job.getJobTitle();
                        cacheUrl = getDownloadFile(jobId, jobTypeId, writeData, fileName);
                        log.info("生成文件成功，临时访问地址:{}", cacheUrl);
                    }
                }

                // 添加日志
                if (success) {
                    logService.addJobLog(jobId, jobTypeId, OperateTypeEnum.EXECUTE_JOB, OperateTypeEnum.EXECUTE_JOB.getOperateDesc() + "-成功", OperateTypeEnum.EXECUTE_JOB.getSuccess(), null);
                } else {
                    executeStatus = StatusEnum.JOB_EXECUTE_STATUS_FAILED;
                    logService.addJobLog(jobId, jobTypeId, OperateTypeEnum.EXECUTE_JOB, OperateTypeEnum.EXECUTE_JOB.getOperateDesc() + "-失败", OperateTypeEnum.EXECUTE_JOB.getFailed(), null);
                }

            } catch (Exception e) {
                log.error("任务处理失败", e);
                executeStatus = StatusEnum.JOB_EXECUTE_STATUS_FAILED;
                logService.addJobLog(jobId, jobTypeId, OperateTypeEnum.EXECUTE_JOB, OperateTypeEnum.EXECUTE_JOB.getOperateDesc() + "-异常", OperateTypeEnum.EXECUTE_JOB.getFailed(), null);
//                messageService.sendToWeiXin(job, null, false);
                exception = true;
                ThreadLocalCache.RESULT_ERROR_INFO_THREAD_LOCAL.set(new ApiException("500", e.getMessage()));
            } finally {
                // 更新任务状态
                jobService.updateExecuteStatus(job, executeStatus);
                distributedLocks.unlock(key);
                ThreadLocalCache.RESULT_THREAD_LOCAL.remove();
                ThreadLocalCache.RESULT_ERROR_INFO_THREAD_LOCAL.remove();
                log.info("处理任务完成！");
            }
        }
    }

    /**
     * 获取zip url
     * @param zipVos 入参
     * @param job job
     * @return url
     */
    private String getZipUrl(List<ZipVo> zipVos, Job job) {
        Set<String> fileCacheUrlSet = new HashSet<>();
        for (int i = 0; i < zipVos.size(); i++) {
            ZipVo zipVo = zipVos.get(i);
            String url = zipVo.getUrl();
            String bizCode = zipVo.getBizCode();
            String fileCacheUrl = qCloudCOSConfiguration.fileCacheUrl(url, bizCode);
            //下载文件
            fileCacheUrlSet.add(fileUtil.downloadFromUrl(url, localTempPath, fileCacheUrl));
        }
        log.info("下载到本地文件集合:{}", fileCacheUrlSet);
        //压缩文件
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String zipName = job.getId() + "-" + job.getJobTitle() + ".zip";
//        zipName = zipName + "批量下载金融评估报告.zip";
        fileUtil.createZip(localTempPath + zipName, fileCacheUrlSet);
        //上传压缩文件,获取压缩文件不可私有url
        String cdnKey = qCloudCOSConfiguration.putObject(zipName);
        String qCloudUrl = qCloudCOSConfiguration.qCloudUrl(cdnKey);
        //删除文件和压缩文件
        fileCacheUrlSet.stream().forEach(url -> {
            File file = new File(url);
            excelAnalysisModelImpl.deleteFile(file);
        });
        File file = new File(localTempPath + zipName);
        excelAnalysisModelImpl.deleteFile(file);
        //获取压缩文件临时下载url
        String cacheUrl = qCloudCOSConfiguration.fileCacheUrl(qCloudUrl);
        log.info("生成压缩文件成功，临时访问地址:{}", cacheUrl);
        // 新增文件
        JobCenterFile jobCenterFile = new JobCenterFile();
        jobCenterFile.setFileCdn(cdnKey);
        jobCenterFile.setFileUrl(qCloudUrl);
        jobCenterFile.setFileAnalysisModel(FileAnalysisModelEnum.MATCH_KEY.getModel());
        jobCenterFile.setFileType(FileTypeEnum.DOWNLOAD_DATA_FILE.getFileType());
        fileService.insert(jobCenterFile, job.getJobId(), ObjectTypeEnum.JOB);
        return cacheUrl;
    }

    /**
     * 需要上传文件执行任务
     * @param jobId 任务ID
     * @param jobTypeId 任务类型ID
     * @param jobTypeParamList 任务类型参数
     * @param needDownloadFile 是否需要下载文件
     * @param entityEnum 接口返回数据的实体类型
     * @param requestUrl 请求接口的URL
     * @param requestMethod 接口的请求方法
     * @param writeData 需要下载的数据文件
     */
    private boolean uploadFile(String jobId, String jobTypeId, List<RequestParam> jobTypeParamList, boolean needDownloadFile, ResponseDataEntityEnum entityEnum, String requestUrl, RequestMethodEnum requestMethod, List<Object> writeData) {
        List<JobCenterFile> jobUploadFileList = fileService.list(jobId, ObjectTypeEnum.JOB, FileTypeEnum.UPLOAD_DATA_FILE);
        for (JobCenterFile uploadFile : jobUploadFileList) {
            File file = qCloudCOSConfiguration.loadObject(uploadFile.getFileCdn());
            // 根据任务上传文件的解析类型解析文件并请求接口
            FileAnalysisModelEnum fileAnalysisModelEnum = FileAnalysisModelEnum.get(uploadFile.getFileAnalysisModel());
            boolean result = false;
            switch (fileAnalysisModelEnum) {
                case IN_TURN:
                    result = inTurn(jobTypeParamList, needDownloadFile, entityEnum, requestUrl, requestMethod, writeData, file);
                    break;
                case MATCH_KEY:
                    result = matchKey(jobTypeId, needDownloadFile, entityEnum, requestUrl, requestMethod, writeData, file);
                    break;
            }
            excelAnalysisModelImpl.deleteFile(file);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取下载文件临时访问地址
     *
     * @param jobId     任务ID
     * @param jobTypeId 任务类型
     * @param writeData 写入的数据
     * @param fileName  文件名称
     * @return 文件临时访问地址
     */
    private String getDownloadFile(String jobId, String jobTypeId, List<Object> writeData, String fileName) {
        String cacheUrl = null;
        // 生成下载文件
        List<JobCenterFile> jobTypeUploadFileList = fileService.list(jobTypeId, ObjectTypeEnum.JOB_TYPE, FileTypeEnum.DOWNLOAD_FILE_TEMP);
        if (!CollectionUtils.isEmpty(jobTypeUploadFileList)) {
            JobCenterFile downTempFile = jobTypeUploadFileList.get(0);
            String cdnPath = downTempFile.getFileCdn();
            fileName = fileName + cdnPath.substring(cdnPath.lastIndexOf("."), cdnPath.length());
            File templateFile = qCloudCOSConfiguration.loadObject(cdnPath);
            File downFile = excelAnalysisModelImpl.createFile(writeData, templateFile, qCloudCOSConfiguration.getLocalTempPath(), fileName);

            // 上传到腾讯云 && 写入库
            String cdnKey = qCloudCOSConfiguration.putObject(fileName);
            log.info("文件上传到腾讯云，cdnKey:{}", cdnKey);

            // 删除本地缓存文件
            excelAnalysisModelImpl.deleteFile(templateFile);
            excelAnalysisModelImpl.deleteFile(downFile);
            String qCloudUrl = qCloudCOSConfiguration.qCloudUrl(cdnKey);
            cacheUrl = qCloudCOSConfiguration.fileCacheUrl(qCloudUrl);

            // 新增文件
            JobCenterFile jobCenterFile = new JobCenterFile();
            jobCenterFile.setFileCdn(cdnKey);
            jobCenterFile.setFileUrl(qCloudUrl);
            jobCenterFile.setFileAnalysisModel(FileAnalysisModelEnum.MATCH_KEY.getModel());
            jobCenterFile.setFileType(FileTypeEnum.DOWNLOAD_DATA_FILE.getFileType());
            fileService.insert(jobCenterFile, jobId, ObjectTypeEnum.JOB);
            log.info("新增下载文件，jobCenterFile：{}", jobCenterFile);
        }
        return cacheUrl;
    }

    /**
     * 无须上传文件时执行任务
     *
     * @param jobParamList     任务的参数列表
     * @param needDownloadFile 是否需要下载文件
     * @param entityEnum       接口返回数据的实体类型
     * @param requestUrl       请求接口的URL
     * @param requestMethod    接口的请求方法
     * @param writeData        上传的数据文件
     */
    private boolean unUploadFile(List<RequestParam> jobParamList, boolean needDownloadFile, ResponseDataEntityEnum entityEnum, String requestUrl, RequestMethodEnum requestMethod, List<Object> writeData) {
        // 无需上传文件，直接根据参数和值执行任务
        if (CollectionUtils.isEmpty(jobParamList)) {
            log.info("无参的任务，requestUrl:{}", requestUrl);
            return request(needDownloadFile, entityEnum, requestUrl, requestMethod, writeData, null);
        } else {
            Map<String, Object> params = new HashMap<>();
            for (RequestParam requestParam : jobParamList) {
                params.put(requestParam.getParamName(), ParamTypeEnum.formatParamValue(requestParam));
            }
            return request(needDownloadFile, entityEnum, requestUrl, requestMethod, writeData, Collections.singletonList(params));
        }
    }


    /**
     * 匹配值获取Excel数据并请求接口
     *
     * @param jobTypeId        任务类型ID
     * @param needDownloadFile 是否需要下载文件
     * @param entityEnum       接口返回数据的实体类型
     * @param requestUrl       请求接口的URL
     * @param requestMethod    接口的请求方法
     * @param writeData        要生成Excel的数据
     * @param file             上传的数据文件
     */
    private boolean matchKey(String jobTypeId, boolean needDownloadFile, ResponseDataEntityEnum entityEnum, String requestUrl, RequestMethodEnum requestMethod, List<Object> writeData, File file) {
        // 获取任务类型上传的模板文件
        List<JobCenterFile> jobTypeUploadFileList = fileService.list(jobTypeId, ObjectTypeEnum.JOB_TYPE, FileTypeEnum.UPLOAD_FILE_TEMP);
        if (!CollectionUtils.isEmpty(jobTypeUploadFileList)) {
            File templateFile = qCloudCOSConfiguration.loadObject(jobTypeUploadFileList.get(0).getFileCdn());
            Map<Integer, ParamBO> paramKeyMap = excelAnalysisModelImpl.getParamKeyFromExcel(templateFile);
            // 分页读取Excel，获取请求数据
            int pageNum = 1, pageSize = 1000;
            List<Map<String, Object>> paramList = excelAnalysisModelImpl.getParamValueFromExcelMatchKeyPaged(file, pageNum, pageSize, paramKeyMap);
            while (!CollectionUtils.isEmpty(paramList)) {
                if (CollectionUtils.isEmpty(paramList)) {
                    break;
                }
                boolean result = request(needDownloadFile, entityEnum, requestUrl, requestMethod, writeData, paramList);
                if (!result) {
                    return false;
                }
                pageNum++;
                paramList = excelAnalysisModelImpl.getParamValueFromExcelMatchKeyPaged(file, pageNum, pageSize, paramKeyMap);
            }
            excelAnalysisModelImpl.deleteFile(templateFile);
            return true;
        }
        return false;
    }

    /**
     * 循序获取Excel数据并请求接口
     *
     * @param jobTypeParamList 任务类型中的请求参数
     * @param needDownloadFile 是否需要下载文件
     * @param entityEnum       接口返回数据的实体类型
     * @param requestUrl       请求接口的URL
     * @param requestMethod    接口的请求方法
     * @param writeData        要生成Excel的数据
     * @param file             上传的数据文件
     */
    private boolean inTurn(List<RequestParam> jobTypeParamList, boolean needDownloadFile, ResponseDataEntityEnum entityEnum, String requestUrl, RequestMethodEnum requestMethod, List<Object> writeData, File file) {
        int pageNum = 1, pageSize = 1000;
        Map<Integer, List<ParamBO>> transformToMap = requestParamService.transformToMap(jobTypeParamList);
        // 分页读取Excel，获取请求数据
        List<Map<String, Object>> paramList = excelAnalysisModelImpl.getParamValueFromExcelInTurnPaged(file, pageNum, pageSize, transformToMap);
        while (!CollectionUtils.isEmpty(paramList)) {
            if (CollectionUtils.isEmpty(paramList)) {
                break;
            }
            boolean result = request(needDownloadFile, entityEnum, requestUrl, requestMethod, writeData, paramList);
            if (!result) {
                return false;
            }
            pageNum++;
            paramList = excelAnalysisModelImpl.getParamValueFromExcelInTurnPaged(file, pageNum, pageSize, transformToMap);
        }
        return true;
    }

    /**
     * 请求接口
     *
     * @param needDownloadFile 是否需要下载文件
     * @param entityEnum       接口返回数据的实体类型
     * @param requestUrl       请求接口的URL
     * @param requestMethod    接口的请求方法
     * @param writeData        要生成Excel的数据
     * @param paramList        请求参数列表
     */
    private boolean request(boolean needDownloadFile, ResponseDataEntityEnum entityEnum, String requestUrl, RequestMethodEnum requestMethod, List<Object> writeData, List<Map<String, Object>> paramList) {
        AtomicBoolean startFromZero = new AtomicBoolean(false);
        if (CollectionUtils.isEmpty(paramList)) {
            return pagingRequest(needDownloadFile, entityEnum, requestUrl, requestMethod, writeData, null, startFromZero);
        } else {
            for (Map<String, Object> params : paramList) {
                boolean result = pagingRequest(needDownloadFile, entityEnum, requestUrl, requestMethod, writeData, params, startFromZero);
                if (!result) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 分页请求
     *
     * @param needDownloadFile 是否需要下载文件
     * @param entityEnum       接口返回数据的实体类型
     * @param requestUrl       请求接口的URL
     * @param requestMethod    接口的请求方法
     * @param writeData        要生成Excel的数据
     * @param params           请求参数
     * @param startFromZero    分页是否从0开始
     * @return boolean
     */
    private boolean pagingRequest(boolean needDownloadFile, ResponseDataEntityEnum entityEnum, String requestUrl,
                                  RequestMethodEnum requestMethod, List<Object> writeData, Map<String, Object> params,
                                  AtomicBoolean startFromZero) {
        Map<String, String> successCode = successConfig.getSuccessCode();
        Either<ApiException, ResponseResult> responseResultEither = _request(needDownloadFile, entityEnum, requestUrl, requestMethod, writeData, params);
        if (responseResultEither.isLeft() || !successCode.containsKey(responseResultEither.getRight().getCode())) {
            log.error("请求接口出现异常，requestUrl:{},params:{}", requestUrl, params);
            return false;
        }
        // 如果有下一页 则继续分页请求下一页数据
        int nextPage = nextPage(entityEnum, responseResultEither, startFromZero);
        if (nextPage < 0) {
            return true;
        } else {
            if (CollectionUtils.isEmpty(params)) {
                params = new HashMap<>();
            }
            params.put("page", nextPage);
            return pagingRequest(needDownloadFile, entityEnum, requestUrl, requestMethod, writeData, params, startFromZero);
        }
    }

    /**
     * 执行请求
     *
     * @param needDownloadFile 是否需要下载文件
     * @param entityEnum       接口返回数据的实体类型
     * @param requestUrl       请求接口的URL
     * @param requestMethod    接口的请求方法
     * @param writeData        要生成Excel的数据
     * @param params           请求参数
     * @return Either
     */
    private Either<ApiException, ResponseResult> _request(boolean needDownloadFile, ResponseDataEntityEnum entityEnum,
                                                          String requestUrl, RequestMethodEnum requestMethod,
                                                          List<Object> writeData, Map<String, Object> params) {
        Either<ApiException, ResponseResult> responseResultEither = null;
        // 请求接口，获取返回数据
        switch (requestMethod) {
            case GET:
                responseResultEither = httpUtil.get(requestUrl, params);
                break;
            case POST:
                responseResultEither = httpUtil.post(requestUrl, params);
                break;
        }
        if (Objects.nonNull(responseResultEither) && responseResultEither.isRight()) {
            ThreadLocalCache.RESULT_THREAD_LOCAL.set(responseResultEither.getRight());
        }
        if (Objects.nonNull(responseResultEither) && responseResultEither.isLeft()) {
            ThreadLocalCache.RESULT_ERROR_INFO_THREAD_LOCAL.set(responseResultEither.getLeft());
        }
        if (needDownloadFile && Objects.nonNull(responseResultEither) && responseResultEither.isRight()) {
            // 需要下载文件的，将接口返回数据暂存，等待统一生成Excel
            chooseAndAdd(entityEnum, writeData, responseResultEither);
        }
        return responseResultEither;
    }

    /**
     * 将返回结果写入list中
     *
     * @param entityEnum           返回体
     * @param writeData            写入的数据
     * @param responseResultEither 返回
     */
    private void chooseAndAdd(ResponseDataEntityEnum entityEnum, List<Object> writeData, Either<ApiException, ResponseResult> responseResultEither) {
        String errorCode = responseResultEither.getRight().getCode();
        Map<String, String> successCode = successConfig.getSuccessCode();
        if (!successCode.containsKey(errorCode)) {
            return;
        }
        switch (entityEnum) {
            case PAGE_HELPER_PAGE_INFO:
                PageInfo pageInfo = JSON.parseObject(JSON.toJSONString(responseResultEither.getRight().getData()), PageInfo.class);
                writeData.addAll(pageInfo.getList());
                break;
            case SPRING_PAGE:
                Page page = JSON.parseObject(JSON.toJSONString(responseResultEither.getRight().getData()), Page.class);
                writeData.addAll(page.getContent());
                break;
            case JSON_ARRAY:
                ResponseResult<List<Map<String, Object>>> obj = responseResultEither.getRight();
                writeData.addAll(obj.getData());
                break;
            case JSON_OBJECT:
                writeData.add(responseResultEither.getRight().getData());
                break;
        }
    }

    /**
     * 判断是否需要分页
     *
     * @param entityEnum           返回体
     * @param responseResultEither 返回
     * @return 有下一页则返回下一页页码，没有则返回-1
     */
    private int nextPage(ResponseDataEntityEnum entityEnum, Either<ApiException, ResponseResult> responseResultEither, AtomicBoolean startFromZero) {
        if (responseResultEither.isLeft()) {
            return -1;
        }
        Map<String, String> successCode = successConfig.getSuccessCode();
        String errorCode = responseResultEither.getRight().getCode();
        if (successCode.containsKey(errorCode)) {
            long nowCount;
            switch (entityEnum) {
                case PAGE_HELPER_PAGE_INFO:
                    PageInfo pageInfo = JSON.parseObject(JSON.toJSONString(responseResultEither.getRight().getData()), PageInfo.class);
                    long total = pageInfo.getTotal();
                    int pageSize = pageInfo.getPageSize();
                    int pageNum = pageInfo.getPageNum();
                    if (pageNum <= 0) {
                        pageNum = 1;
                    }
                    nowCount = pageSize * pageNum;
                    return nowCount >= total ? -1 : pageNum + 1;
                case SPRING_PAGE:
                    Page page = JSON.parseObject(JSON.toJSONString(responseResultEither.getRight().getData()), Page.class);
                    long totalElements = page.getTotalElements();
                    int number = page.getNumber();
                    int size = page.getSize();
                    if (number <= 0) {
                        nowCount = size * (number + 1);
                        startFromZero.compareAndSet(false, true);
                    } else {
                        if (startFromZero.get()) {
                            nowCount = size * (number + 1);
                        } else {
                            nowCount = size * number;
                        }
                    }
                    return nowCount >= totalElements ? -1 : number + 1;
            }
        }
        return -1;
    }

}
