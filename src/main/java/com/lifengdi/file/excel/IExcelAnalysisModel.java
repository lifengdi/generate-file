package com.lifengdi.file.excel;

import com.lifengdi.model.ParamBO;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 接口类
 * @author 李锋镝
 * @date Create at 10:42 2019/4/9
 */
public interface IExcelAnalysisModel {

    /**
     * 从Excel中获取获取指定列的参数，并按照paramKey封装
     *
     * @param pageNum    页码（从1开始）,第一页的数据从Excel的第二行开始读取(即从1开始)
     * @param pageSize   每页数量
     * @param sourceFile 源文件
     * @param paramKey   参数key
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> getParamValueFromExcelMatchKeyPaged(File sourceFile, int pageNum, int pageSize, Map<Integer, ParamBO> paramKey);

    /**
     * 从Excel中获取参数名(只取第二行的数据)
     *
     * @param sourceFile 源文件
     * @return Map<Integer,ParamBO> key:所在单元格的列数(从0开始)，value:参数名
     */
    Map<Integer, ParamBO> getParamKeyFromExcel(File sourceFile);

    /**
     * 创建文件并写入数据，不支持分页，自定义map中的key
     *
     * @param data       要写入的数据
     * @param sourceFile 模板源文件
     * @param path       生成文件的路径
     * @param name       生成文件的名称
     * @return 创建的文件
     */
    File createFile(Map<String, Object> data, File sourceFile, String path, String name);

    /**
     * 创建文件并写入数据，不支持分页，默认map中的key为list
     *
     * @param data       要写入的数据
     * @param sourceFile 模板源文件
     * @param path       生成文件的路径
     * @param name       生成文件的名称
     * @return 创建的文件
     */
    File createFile(List<Object> data, File sourceFile, String path, String name);

    /**
     * 从Excel中获取获取指定列的参数，并按照paramKey封装
     *
     * @param pageNum    页码（从1开始）,第一页的数据从Excel的第二行开始读取(即从1开始)
     * @param pageSize   每页数量
     * @param sourceFile 源文件
     * @param paramKey   参数key
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> getParamValueFromExcelInTurnPaged(File sourceFile, int pageNum, int pageSize, Map<Integer, List<ParamBO>> paramKey);

    /**
     * 从Excel中获取获取指定列的参数，并按照paramKey封装
     *
     * @param sourceFile 源文件
     * @param begin      开始行数
     * @param end        结束行数
     * @param paramKey   参数key
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> getParamValueFromExcel(File sourceFile, int begin, int end, Map<Integer, List<ParamBO>> paramKey, boolean useValue);


    /**
     * 创建路径
     *
     * @param path 路径
     */
    void createdPath(String path);

    /**
     * 删除文件
     *
     * @param path 文件路径
     * @param name 文件名
     */
    void deleteFile(String path, String name);

    /**
     * 读取cell
     *
     * @param cell   cell
     * @param format 日期格式
     * @return value
     */
    Object getCellValue(Cell cell, String format);
}
