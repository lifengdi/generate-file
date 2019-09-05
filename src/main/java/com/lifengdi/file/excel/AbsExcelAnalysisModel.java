package com.lifengdi.file.excel;

import com.lifengdi.exception.AppException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * 抽象类
 * @author 李锋镝
 * @date Create at 10:42 2019/4/9
 */
public abstract class AbsExcelAnalysisModel implements IExcelAnalysisModel {

    /**
     * 创建路径
     *
     * @param path 路径
     */
    public void createdPath(String path) {
        File newFile = new File(path);
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
    }

    /**
     * 删除文件
     *
     * @param path 文件路径
     * @param name 文件名
     */
    public void deleteFile(String path, String name) {
        File file = new File(path + name);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除文件
     * @param file 文件
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 读取cell
     *
     * @param cell   cell
     * @param format 日期格式
     * @return value
     */
    public Object getCellValue(Cell cell, String format) {
        CellType cellType = cell.getCellTypeEnum();
        DataFormatter formatter = new DataFormatter();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    if (StringUtils.isNotBlank(format)) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                        return simpleDateFormat.format(cell.getDateCellValue());
                    }
                    return formatter.formatCellValue(cell);
                }
                return formatter.formatCellValue(cell);
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getStringCellValue();
            case BLANK:
                return null;
            default:
                return null;
        }
    }

    /**
     * 检查文件格式
     * @param fileName
     */
    protected void checkFileFormat(String fileName) {
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw AppException.FILE_FORMAT_ERROR.build();
        }
    }
}
