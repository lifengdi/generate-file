package com.lifengdi.job.mapper;

import com.lifengdi.job.entity.JobLog;
import com.lifengdi.job.example.JobLogCondition;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 任务操作日志
 * 
 * @author goddess
 * 2019-09-05
 */
public interface JobLogMapper {
    /**
     * 根据指定的条件获取数据库记录数
     *
     * @param condition JobLogCondition
     * 
     * @return long 记录数
     */
    long countByExample(JobLogCondition condition);

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
     * @param record JobLog
     * 
     * @return int 影响行数
     */
    int insert(JobLog record);

    /**
     * 动态字段,写入数据库记录
     *
     * @param record JobLog
     * 
     * @return int 影响行数
     */
    int insertSelective(JobLog record);

    /**
     * 根据指定的条件查询符合条件的数据库记录
     *
     * @param condition JobLogCondition
     * 
     * @return List<JobLog>
     */
    List<JobLog> selectByExample(JobLogCondition condition);

    /**
     * 根据指定主键获取一条数据库记录
     *
     * @param id Long
     * 
     * @return JobLog
     */
    JobLog selectByPrimaryKey(Long id);

    /**
     * 动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record JobLog
     * @param condition JobLogCondition
     * 
     * @return int 影响行数
     */
    int updateByExampleSelective(@Param("record") JobLog record, @Param("example") JobLogCondition condition);

    /**
     * 根据指定的条件来更新符合条件的数据库记录
     *
     * @param record JobLog
     * @param condition JobLogCondition
     * 
     * @return int 影响行数
     */
    int updateByExample(@Param("record") JobLog record, @Param("example") JobLogCondition condition);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record JobLog
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKeySelective(JobLog record);

    /**
     * 根据主键来更新符合条件的数据库记录
     *
     * @param record JobLog
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKey(JobLog record);
}