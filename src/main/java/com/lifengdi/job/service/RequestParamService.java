package com.lifengdi.job.service;

import com.lifengdi.enums.GlobalEnum;
import com.lifengdi.enums.ObjectTypeEnum;
import com.lifengdi.enums.ParamTypeEnum;
import com.lifengdi.enums.StatusEnum;
import com.lifengdi.exception.AppException;
import com.lifengdi.job.entity.RequestParam;
import com.lifengdi.job.example.RequestParamCondition;
import com.lifengdi.job.mapper.IRequestParamMapper;
import com.lifengdi.model.ParamBO;
import com.lifengdi.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 李锋镝
 * @date Create at 17:22 2019/4/2
 */
@Service
@Slf4j
public class RequestParamService {
    @Resource
    private IRequestParamMapper iRequestParamMapper;

    /**
     * @param requestParam RequestParam
     * @param user         user
     * @param objId        objId
     * @return RequestParam
     */
    public RequestParam insertOrUpdate(RequestParam requestParam, User user, String objId, ObjectTypeEnum objectTypeEnum) {

        Objects.requireNonNull(requestParam);
        Objects.requireNonNull(requestParam.getParamName());
        Objects.requireNonNull(requestParam.getParamTitle());
        Objects.requireNonNull(objId);

        if (!ParamTypeEnum.judgeParamType(requestParam.getParamType())) {
            throw AppException.PARAM_TYPE_NOT_FOUND.builds(requestParam.getParamType());
        }

        iWithDefault(requestParam, user);

        requestParam.setObjId(objId);
        requestParam.setObjType(objectTypeEnum.getType());

        iRequestParamMapper.insertOrUpdateSelective(requestParam);
        return requestParam;
    }

    /**
     * 新增时设置默认值
     *
     * @param requestParam RequestParam
     * @param user         user
     */
    private void iWithDefault(RequestParam requestParam, User user) {
        withCreatedUser(requestParam, user);
        withUpdatedUser(requestParam, user);
        requestParam.setStatus(StatusEnum.COMMON_AUDITED.getStatus());
        requestParam.setDeleted(GlobalEnum.NO.getValue());
    }

    /**
     * 设置CreatedUser
     *
     * @param requestParam RequestParam
     * @param user         user
     */
    private void withCreatedUser(RequestParam requestParam, User user) {
        if (Objects.nonNull(user)) {
            requestParam.setCreatedUserId(user.getUserId());
            requestParam.setCreatedUserPhone(user.getPhone());
            requestParam.setCreatedUserName(user.getUserName());
        }
    }

    /**
     * 设置UpdatedUser
     *
     * @param requestParam RequestParam
     * @param user         user
     */
    private void withUpdatedUser(RequestParam requestParam, User user) {
        if (Objects.nonNull(user)) {
            requestParam.setUpdatedUserId(user.getUserId());
            requestParam.setUpdatedUserName(user.getUserName());
            requestParam.setUpdatedUserPhone(user.getPhone());
        }
    }

    /**
     * 根据对象ID和对象类型 批量删除
     *
     * @param objId          objId
     * @param objectTypeEnum see {@linkplain ObjectTypeEnum}
     * @param user           user
     */
    public void batchDelete(String objId, ObjectTypeEnum objectTypeEnum, User user) {
        Objects.requireNonNull(objId);
        Objects.requireNonNull(objectTypeEnum);

        RequestParamCondition condition = new RequestParamCondition();
        condition.createCriteria()
                .andObjIdEqualTo(objId)
                .andObjTypeEqualTo(objectTypeEnum.getType())
                .andDeletedEqualTo(GlobalEnum.NO.getValue());

        RequestParam record = new RequestParam();
        record.setDeleted(GlobalEnum.YES.getValue());
        withUpdatedUser(record, user);

        iRequestParamMapper.updateByExampleSelective(record, condition);
    }

    /**
     * 根据对象ID和对象类型查询
     *
     * @param objId          objId
     * @param objectTypeEnum see {@linkplain ObjectTypeEnum}
     */
    public List<RequestParam> list(String objId, ObjectTypeEnum objectTypeEnum) {
        Objects.requireNonNull(objId);
        Objects.requireNonNull(objectTypeEnum);

        RequestParamCondition condition = new RequestParamCondition();
        condition.createCriteria().andObjIdEqualTo(objId)
                .andObjTypeEqualTo(objectTypeEnum.getType())
                .andDeletedEqualTo(GlobalEnum.NO.getValue());

        return iRequestParamMapper.selectByExample(condition);
    }

    /**
     * List<RequestParam> >> List<ParamBO>
     * @param requestParamList List<RequestParam>
     * @return List<ParamBO>
     */
    public Map<Integer, List<ParamBO>> transformToMap(List<RequestParam> requestParamList) {
        if (CollectionUtils.isEmpty(requestParamList)) {
            return null;
        }
        Map<Integer, List<ParamBO>> map = new HashMap<>();
        List<ParamBO> paramBOList;
        for (RequestParam requestParam : requestParamList) {
            String paramValue = requestParam.getParamValue();
            if (StringUtils.isNumeric(paramValue)) {
                if (map.containsKey(Integer.valueOf(paramValue))) {
                    paramBOList = map.get(Integer.valueOf(paramValue));
                } else {
                    paramBOList = new ArrayList<>();
                }
                paramBOList.add(getParamBO(requestParam));
                map.put(Integer.valueOf(paramValue), paramBOList);
            }
        }
        return map;
    }

    public ParamBO getParamBO(RequestParam requestParam) {
        ParamBO paramBO = new ParamBO();
        paramBO.setParamKey(requestParam.getParamName());
        paramBO.setParamFormat(requestParam.getParamFormat());
        paramBO.setParamType(ParamTypeEnum.get(requestParam.getParamType()));
        paramBO.setParamValue(requestParam.getParamValue());
        return paramBO;
    }
}
