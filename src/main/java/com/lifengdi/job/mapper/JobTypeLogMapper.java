package com.lifengdi.job.mapper;

import com.lifengdi.job.entity.JobTypeLog;
import com.lifengdi.job.example.JobTypeLogCondition;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 任务类型操作日志
 * 
 * @author goddess
 * 2019-09-05
 */
public interface JobTypeLogMapper {
    /**
     * 根据指定的条件获取数据库记录数
     *
     * @param condition JobTypeLogCondition
     * 
     * @return long 记录数
     */
    long countByExample(JobTypeLogCondition condition);

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
     * @param record JobTypeLog
     * 
     * @return int 影响行数
     */
    int insert(JobTypeLog record);

    /**
     * 动态字段,写入数据库记录
     *
     * @param record JobTypeLog
     * 
     * @return int 影响行数
     */
    int insertSelective(JobTypeLog record);

    /**
     * 根据指定的条件查询符合条件的数据库记录
     *
     * @param condition JobTypeLogCondition
     * 
     * @return List<JobTypeLog>
     */
    List<JobTypeLog> selectByExample(JobTypeLogCondition condition);

    /**
     * 根据指定主键获取一条数据库记录
     *
     * @param id Long
     * 
     * @return JobTypeLog
     */
    JobTypeLog selectByPrimaryKey(Long id);

    /**
     * 动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record JobTypeLog
     * @param condition JobTypeLogCondition
     * 
     * @return int 影响行数
     */
    int updateByExampleSelective(@Param("record") JobTypeLog record, @Param("example") JobTypeLogCondition condition);

    /**
     * 根据指定的条件来更新符合条件的数据库记录
     *
     * @param record JobTypeLog
     * @param condition JobTypeLogCondition
     * 
     * @return int 影响行数
     */
    int updateByExample(@Param("record") JobTypeLog record, @Param("example") JobTypeLogCondition condition);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record JobTypeLog
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKeySelective(JobTypeLog record);

    /**
     * 根据主键来更新符合条件的数据库记录
     *
     * @param record JobTypeLog
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKey(JobTypeLog record);
}