package com.lifengdi.file.excel.impl;

import com.alibaba.fastjson.JSONObject;
import com.lifengdi.exception.AppException;
import com.lifengdi.file.excel.AbsExcelAnalysisModel;
import com.lifengdi.model.ParamBO;
import lombok.extern.slf4j.Slf4j;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.*;

/**
 * @author 李锋镝
 * @date Create at 16:22 2019/4/8
 */
@Component
@Slf4j
public class ExcelAnalysisModelImpl extends AbsExcelAnalysisModel {

    public static void main(String args[]) {
        File file = null;
        try {

            file = new File("/Users/goddess/example.xlsx");
            Map<String, Object> data = new HashMap<>();
            List<JSONObject> list = new ArrayList<>();
            JSONObject sex = new JSONObject();
            sex.put("name", "男");
            for (int i=0; i<100000; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", i);
                jsonObject.put("name", "qwe" + i);
                jsonObject.put("age", "10");
                jsonObject.put("sex", sex);
                list.add(jsonObject);
            }
            data.put("list", list);
            long b = System.currentTimeMillis();
//            System.out.println(write(data, file, "/Users/goddess/", "123.xlsx").getAbsolutePath());;

            long e = System.currentTimeMillis();
            System.out.println(e - b);
//            Map<Integer, Object> map = getParamKeyFromExcel(new File("/Users/goddess/123.xlsx"));
//            Map<Integer, ParamBO> paramBOMap = new HashMap<>();
//            for (Map.Entry<Integer, Object> entry : map.entrySet()) {
//                ParamBO paramBO = new ParamBO();
//                paramBO.setParamKey(entry.getValue().toString());
//                paramBOMap.put(entry.getKey(), paramBO);
//            }
//            getParamValueFromExcel(new File("/Users/goddess/123.xlsx"), 2, 100, paramBOMap).forEach(m -> System.out.println(m));
//            getParamValueFromExcelPaged(new File("/Users/goddess/123.xlsx"), 0, 100, paramBOMap).forEach(m -> System.out.println(m));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 从Excel中获取获取指定列的参数，并按照paramKey封装
     * @param pageNum 页码（从1开始）,第一页的数据从Excel的第二行开始读取
     * @param pageSize 每页数量
     * @param sourceFile 源文件
     * @param paramKey 参数key
     * @return List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> getParamValueFromExcelMatchKeyPaged(File sourceFile, int pageNum, int pageSize, Map<Integer, ParamBO> paramKey) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        int startRow = (pageNum - 1) * pageSize + 1;
        int endRow = pageNum * pageSize + 1;
        Map<Integer, List<ParamBO>> listMap = new HashMap<>();
        for (Map.Entry<Integer, ParamBO> entry : paramKey.entrySet()) {
            listMap.put(entry.getKey(), Collections.singletonList(entry.getValue()));
        }
        return getParamValueFromExcel(sourceFile, startRow, endRow, listMap, false);
    }

    /**
     * 从Excel中获取获取指定列的参数，并按照paramKey封装
     * @param pageNum 页码（从1开始）,第一页的数据从Excel的第二行开始读取
     * @param pageSize 每页数量
     * @param sourceFile 源文件
     * @param paramKey 参数key
     * @return List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> getParamValueFromExcelInTurnPaged(File sourceFile, int pageNum, int pageSize, Map<Integer, List<ParamBO>> paramKey) {
        int startRow = (pageNum - 1) * pageSize + 1;
        int endRow = pageNum * pageSize + 1;
        return getParamValueFromExcel(sourceFile, startRow, endRow, paramKey, true);
    }

    /**
     * 从Excel中获取获取指定列的参数，并按照paramKey封装
     * @param sourceFile 源文件
     * @param begin 开始行数
     * @param end 结束行数
     * @param paramKey 参数key
     * @param useValue 是否通过比较paramValue和单元格行数是否相等来获取参数
     * @return List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> getParamValueFromExcel(File sourceFile, int begin, int end, Map<Integer, List<ParamBO>> paramKey, boolean useValue) {
        checkFileFormat(sourceFile.getName());
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map;
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            Workbook workBook = WorkbookFactory.create(inputStream);
            Sheet sheet = workBook.getSheetAt(0);
            if (sheet != null) {
                if (begin > sheet.getLastRowNum()) {
                    return list;
                }
                if (end > sheet.getLastRowNum()) {
                    end = sheet.getLastRowNum() + 1;
                }
                for (int rowNum = begin; rowNum < end; rowNum++) {
                    // 忽略标题行
                    if (rowNum == 0) {
                        continue;
                    }
                    Row row = sheet.getRow(rowNum);
                    if (Objects.isNull(row)) {
                        continue;
                    }
                    map = new HashMap<>();
                    for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                        List<ParamBO> paramBOList = paramKey.get(i);
                        if (!CollectionUtils.isEmpty(paramBOList)) {
                            for (ParamBO paramBO : paramBOList) {
                                if (useValue && paramBO.getParamValue().equals(String.valueOf(i))) {
                                    map.put(paramBO.getParamKey(), getCellValue(row.getCell(i), paramBO.getParamFormat()));
                                }
                                if (!useValue) {
                                    map.put(paramBO.getParamKey(), getCellValue(row.getCell(i), paramBO.getParamFormat()));
                                }
                            }
                        }
                    }
                    list.add(map);
                }

            }
        } catch (Exception e) {
            log.error("读取文件异常", e);
            throw AppException.FILE_ANALYSIS_EXCEPTION.build();
        } finally {
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream close error", e);
                }
            }
        }
        return list;
    }

    /**
     * 从Excel中获取参数名(只取第二行的数据)
     * @param sourceFile 源文件
     * @return Map<Integer, Object> key:所在单元格的列数(从0开始)，value:参数名
     */
    @Override
    public Map<Integer, ParamBO> getParamKeyFromExcel(File sourceFile) {
        String fileName = sourceFile.getName();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw AppException.FILE_FORMAT_ERROR.build();
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        Map<Integer, ParamBO> result = new HashMap<>();
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            Workbook wb;
            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }
            Sheet sheet = wb.getSheetAt(0);
            if (sheet != null) {
                Row row = sheet.getRow(1);
                for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
                    result.put(i, new ParamBO((getCellValue(row.getCell(i), null).toString())));
                }
            }
        } catch (Exception e) {
            log.error("读取文件异常", e);
        } finally {
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream close error", e);
                }
            }
        }
        return result;
    }

    /**
     * 创建文件并写入数据(不支持分页)
     *
     * @param data       要写入的数据
     * @param sourceFile 模板源文件
     * @param path       生成文件的路径
     * @param name       生成文件的名称
     * @return 创建的文件
     */
    @Override
    public File createFile(Map<String, Object> data, File sourceFile, String path, String name) {
        XLSTransformer xlsTransformer = new XLSTransformer();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File newFile = new File(path + name);
        try {
            createdPath(path);
            inputStream = new BufferedInputStream(new FileInputStream(sourceFile));
            outputStream = new FileOutputStream(newFile);
            Workbook workbook = xlsTransformer.transformXLS(inputStream, data);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("创建文件出错", e);
            throw AppException.CREATE_FILE_EXCEPTION.build();
        } finally {
            if (Objects.nonNull(outputStream)) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("outputStream close error", e);
                }
            }
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("inputStream close error", e);
                }
            }
        }
        return newFile;
    }

    public void write(Map<String, Object> data, File sourceFile, String path, String name) {
        XLSTransformer xlsTransformer = new XLSTransformer();
        try {
            createdPath(path);
            xlsTransformer.transformXLS(sourceFile.getAbsolutePath(), data, path + name);
        } catch (Exception e) {
            log.error("创建文件出错", e);
        }
    }

    @Override
    public File createFile(List<Object> data, File sourceFile, String path, String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", data);
        return createFile(map, sourceFile, path, name);
    }
}
