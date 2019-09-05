package com.lifengdi.job.mapper;

import com.lifengdi.job.entity.RequestParam;

/**
 * 请求参数
 * 
 * @author goddess
 * 2019-04-02
 */
public interface IRequestParamMapper extends RequestParamMapper {

    /**
     * 动态字段,写入数据库记录
     *
     * @param record RequestParam
     * 
     * @return int 影响行数
     */
    int insertOrUpdateSelective(RequestParam record);

}