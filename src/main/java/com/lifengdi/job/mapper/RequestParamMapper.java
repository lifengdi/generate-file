package com.lifengdi.job.mapper;

import com.lifengdi.job.entity.RequestParam;
import com.lifengdi.job.example.RequestParamCondition;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 请求参数
 * 
 * @author goddess
 * 2019-09-05
 */
public interface RequestParamMapper {
    /**
     * 根据指定的条件获取数据库记录数
     *
     * @param condition RequestParamCondition
     * 
     * @return long 记录数
     */
    long countByExample(RequestParamCondition condition);

    /**
     * 根据主键删除数据库的记录
     *
     * @param id Long
     * 
     * @return int 影响行数
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新写入数据库记录
     *
     * @param record RequestParam
     * 
     * @return int 影响行数
     */
    int insert(RequestParam record);

    /**
     * 动态字段,写入数据库记录
     *
     * @param record RequestParam
     * 
     * @return int 影响行数
     */
    int insertSelective(RequestParam record);

    /**
     * 根据指定的条件查询符合条件的数据库记录
     *
     * @param condition RequestParamCondition
     * 
     * @return List<RequestParam>
     */
    List<RequestParam> selectByExample(RequestParamCondition condition);

    /**
     * 根据指定主键获取一条数据库记录
     *
     * @param id Long
     * 
     * @return RequestParam
     */
    RequestParam selectByPrimaryKey(Long id);

    /**
     * 动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record RequestParam
     * @param condition RequestParamCondition
     * 
     * @return int 影响行数
     */
    int updateByExampleSelective(@Param("record") RequestParam record, @Param("example") RequestParamCondition condition);

    /**
     * 根据指定的条件来更新符合条件的数据库记录
     *
     * @param record RequestParam
     * @param condition RequestParamCondition
     * 
     * @return int 影响行数
     */
    int updateByExample(@Param("record") RequestParam record, @Param("example") RequestParamCondition condition);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record RequestParam
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKeySelective(RequestParam record);

    /**
     * 根据主键来更新符合条件的数据库记录
     *
     * @param record RequestParam
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKey(RequestParam record);
}