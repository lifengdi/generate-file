package com.lifengdi.job.mapper;

import com.lifengdi.job.entity.Job;
import com.lifengdi.job.example.JobCondition;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 任务类型
 * 
 * @author goddess
 * 2019-09-05
 */
public interface JobMapper {
    /**
     * 根据指定的条件获取数据库记录数
     *
     * @param condition JobCondition
     * 
     * @return long 记录数
     */
    long countByExample(JobCondition condition);

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
     * @param record Job
     * 
     * @return int 影响行数
     */
    int insert(Job record);

    /**
     * 动态字段,写入数据库记录
     *
     * @param record Job
     * 
     * @return int 影响行数
     */
    int insertSelective(Job record);

    /**
     * 根据指定的条件查询符合条件的数据库记录
     *
     * @param condition JobCondition
     * 
     * @return List<Job>
     */
    List<Job> selectByExample(JobCondition condition);

    /**
     * 根据指定主键获取一条数据库记录
     *
     * @param id Long
     * 
     * @return Job
     */
    Job selectByPrimaryKey(Long id);

    /**
     * 动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record Job
     * @param condition JobCondition
     * 
     * @return int 影响行数
     */
    int updateByExampleSelective(@Param("record") Job record, @Param("example") JobCondition condition);

    /**
     * 根据指定的条件来更新符合条件的数据库记录
     *
     * @param record Job
     * @param condition JobCondition
     * 
     * @return int 影响行数
     */
    int updateByExample(@Param("record") Job record, @Param("example") JobCondition condition);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record Job
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKeySelective(Job record);

    /**
     * 根据主键来更新符合条件的数据库记录
     *
     * @param record Job
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKey(Job record);
}