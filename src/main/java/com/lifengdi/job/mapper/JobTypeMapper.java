package com.lifengdi.job.mapper;

import com.lifengdi.job.entity.JobType;
import com.lifengdi.job.example.JobTypeCondition;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 任务类型
 * 
 * @author goddess
 * 2019-09-05
 */
public interface JobTypeMapper {
    /**
     * 根据指定的条件获取数据库记录数
     *
     * @param condition JobTypeCondition
     * 
     * @return long 记录数
     */
    long countByExample(JobTypeCondition condition);

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
     * @param record JobType
     * 
     * @return int 影响行数
     */
    int insert(JobType record);

    /**
     * 动态字段,写入数据库记录
     *
     * @param record JobType
     * 
     * @return int 影响行数
     */
    int insertSelective(JobType record);

    /**
     * 根据指定的条件查询符合条件的数据库记录
     *
     * @param condition JobTypeCondition
     * 
     * @return List<JobType>
     */
    List<JobType> selectByExample(JobTypeCondition condition);

    /**
     * 根据指定主键获取一条数据库记录
     *
     * @param id Long
     * 
     * @return JobType
     */
    JobType selectByPrimaryKey(Long id);

    /**
     * 动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record JobType
     * @param condition JobTypeCondition
     * 
     * @return int 影响行数
     */
    int updateByExampleSelective(@Param("record") JobType record, @Param("example") JobTypeCondition condition);

    /**
     * 根据指定的条件来更新符合条件的数据库记录
     *
     * @param record JobType
     * @param condition JobTypeCondition
     * 
     * @return int 影响行数
     */
    int updateByExample(@Param("record") JobType record, @Param("example") JobTypeCondition condition);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record JobType
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKeySelective(JobType record);

    /**
     * 根据主键来更新符合条件的数据库记录
     *
     * @param record JobType
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKey(JobType record);
}