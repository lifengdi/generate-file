package com.lifengdi.job.mapper;

import com.lifengdi.job.entity.JobCenterFile;
import com.lifengdi.job.example.JobCenterFileCondition;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 文件
 * 
 * @author goddess
 * 2019-09-05
 */
public interface JobCenterFileMapper {
    /**
     * 根据指定的条件获取数据库记录数
     *
     * @param condition JobCenterFileCondition
     * 
     * @return long 记录数
     */
    long countByExample(JobCenterFileCondition condition);

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
     * @param record JobCenterFile
     * 
     * @return int 影响行数
     */
    int insert(JobCenterFile record);

    /**
     * 动态字段,写入数据库记录
     *
     * @param record JobCenterFile
     * 
     * @return int 影响行数
     */
    int insertSelective(JobCenterFile record);

    /**
     * 根据指定的条件查询符合条件的数据库记录
     *
     * @param condition JobCenterFileCondition
     * 
     * @return List<JobCenterFile>
     */
    List<JobCenterFile> selectByExample(JobCenterFileCondition condition);

    /**
     * 根据指定主键获取一条数据库记录
     *
     * @param id Long
     * 
     * @return JobCenterFile
     */
    JobCenterFile selectByPrimaryKey(Long id);

    /**
     * 动态根据指定的条件来更新符合条件的数据库记录
     *
     * @param record JobCenterFile
     * @param condition JobCenterFileCondition
     * 
     * @return int 影响行数
     */
    int updateByExampleSelective(@Param("record") JobCenterFile record, @Param("example") JobCenterFileCondition condition);

    /**
     * 根据指定的条件来更新符合条件的数据库记录
     *
     * @param record JobCenterFile
     * @param condition JobCenterFileCondition
     * 
     * @return int 影响行数
     */
    int updateByExample(@Param("record") JobCenterFile record, @Param("example") JobCenterFileCondition condition);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录
     *
     * @param record JobCenterFile
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKeySelective(JobCenterFile record);

    /**
     * 根据主键来更新符合条件的数据库记录
     *
     * @param record JobCenterFile
     * 
     * @return int 影响行数
     */
    int updateByPrimaryKey(JobCenterFile record);
}