package com.lifengdi.generatefile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.lifengdi.GenerateFileApplication;
import com.lifengdi.file.excel.easyexcel.listener.DemoDataListener;
import com.lifengdi.model.excel.Demo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author 李锋镝
 * @date Create at 15:11 2019/10/23
 */
@SpringBootTest(classes = GenerateFileApplication.class)
public class EasyExcelTest extends AbstractTestNGSpringContextTests {

    /**
     * 最简单的读
     * <p>1. 创建excel对应的实体对象 参照{@link com.lifengdi.model.excel.Demo}
     * <p>2. 由于默认异步读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>3. 直接读即可
     */
    @Test
    public void simpleRead() {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法1：
        String fileName = "/Users/goddess/5ccfcec152000e0001aee237_20190506140600.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, Demo.class, new DemoDataListener()).sheet().doRead();

        // 写法2：
//        fileName = "/Users/goddess/5ccfcec152000e0001aee237_20190506140600.xlsx";
        ExcelReader excelReader = EasyExcel.read(fileName, Demo.class, new DemoDataListener()).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
    }
}
