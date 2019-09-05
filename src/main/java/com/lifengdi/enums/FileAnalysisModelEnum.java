package com.lifengdi.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件解析模式
 * @author 李锋镝
 * @date Create at 15:08 2019/4/3
 */
@Getter
public enum FileAnalysisModelEnum {

    /**
     * 例如：List<List> list
     *  for (list)
     *     for (int i=0; i< listItem.size(); i++)
     *          listItem.get(i)
     * 不推荐
     */
    IN_TURN("in_turn", "循序解析")
    /**
     * 例如：List<JSON> list
     * for (list)
     *  listItem.match_key
     */
    , MATCH_KEY("match_key", "匹配值")
    ;

    private String model;

    private String desc;

    FileAnalysisModelEnum(String model, String desc) {
        this.model = model;
        this.desc = desc;
    }

    public static boolean judgeFileAnalysisModel(String model) {
        for (FileAnalysisModelEnum fileAnalysisModelEnum : values()) {
            if (fileAnalysisModelEnum.model.equals(model)) {
                return true;
            }
        }
        return false;
    }

    public static FileAnalysisModelEnum get(String model) {
        for (FileAnalysisModelEnum fileAnalysisModelEnum : values()) {
            if (fileAnalysisModelEnum.model.equals(model)) {
                return fileAnalysisModelEnum;
            }
        }
        return MATCH_KEY;
    }

    public static List<Map<String, Object>> keyValue() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (FileAnalysisModelEnum fileAnalysisModelEnum : values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", fileAnalysisModelEnum.model);
            map.put("desc", fileAnalysisModelEnum.desc);
            list.add(map);
        }
        return list;
    }

}
