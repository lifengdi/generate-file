package com.lifengdi.file.excel.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.lifengdi.model.excel.Demo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李锋镝
 * @date Create at 15:08 2019/10/23
 */
@Slf4j
public class DemoDataListener extends AnalysisEventListener<Demo> {

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<Demo> list = new ArrayList<>();

    @Override
    public void invoke(Demo demo, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(demo));
        list.add(demo);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        log.info("存储数据库成功！");
    }
}
