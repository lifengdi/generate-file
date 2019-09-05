package com.lifengdi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;


/**
 * 生成唯一key
 * @author 李锋镝
 * @date Create at 13:45 2018/12/6
 */
@Component
@Slf4j
public class GeneratedKey {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 生成24位的16进制的key
     * @return key
     */
    public String generatorKey() {

        return GeneratorId.get().toString();
    }


    public static void main(String[] args) {
        GeneratedKey generatedKey = new GeneratedKey();
        System.out.println(generatedKey.generatorKey());
    }

    /**
     * 获取当前机器ID
     * @return MachineIdentifier
     */
    public int getMachineIdentifier() {
        return GeneratorId.getGeneratedMachineIdentifier();
    }

    /**
     * 获取当前进程ID
     * @return ProcessIdentifier
     */
    public int getProcessIdentifier() {
        return GeneratorId.getGeneratedProcessIdentifier();
    }

    /**
     * 获取当前项目唯一ID
     * @return ProjectIdentifier
     */
    public String getProjectIdentifier() {
        StringJoiner joiner = new StringJoiner("_");
        joiner.add(applicationName);
        joiner.add(String.valueOf(getMachineIdentifier()));
        joiner.add(String.valueOf(getProcessIdentifier()));
        return joiner.toString();
    }

}
