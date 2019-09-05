package com.lifengdi.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 */
@Component
@Slf4j
public class FileUtil {

    /**
     * 下载文件到指定目录下
     * @param url 本地文件url
     * @param temUrl 文件远程url
     * @param dir 存放文件目录
     */
    public String downloadFromUrl(String url, String dir, String temUrl) {
        String path = "";
        try {
            URL httpUrl = new URL(temUrl);
            String fileName = getFileNameFromUrl(url);
            File f = new File(dir + fileName);
            FileUtils.copyURLToFile(httpUrl, f);
            path = dir + fileName;
            log.info("下载文件path:{}", path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 获取文件名称
     * @param url 文件url
     * @return 文件名称
     */
    public String getFileNameFromUrl(String url){
        String name = System.currentTimeMillis() + ".X";
        int index = url.lastIndexOf("/");
        if(index > 0){
            name = url.substring(index + 1);
            if(name.trim().length() > 0){
                return name;
            }
        }
        return name;
    }

    /**
     * 压缩文件
     * @param zipName 压缩后文件名
     * @param fileCacheUrlSet 被压缩文件url
     */
    public void createZip(String zipName,Set<String> fileCacheUrlSet) {
        try {
            //文件输出流
            OutputStream os = new FileOutputStream(zipName);
            //创建文件压缩流（处理流|过滤流）
            ZipOutputStream zip = new ZipOutputStream(os);
            //将文件输出缓冲流
            BufferedOutputStream bos = new BufferedOutputStream(zip);
            fileCacheUrlSet.forEach(url -> {
                //添加实体
                try {
                    zip.putNextEntry(new ZipEntry(url));
                    InputStream in = new FileInputStream(url);
                    byte[] buffer = new byte[4096];
                    int len;
                    while((len = in.read(buffer)) != -1){
                        bos.write(buffer, 0, len);
                    }
                    //带缓冲的流需要清空缓冲区，强行把缓冲区的数据写到数据流中
                    bos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            zip.closeEntry();
            zip.finish();
        } catch (Exception e) {
            log.error("压缩文件异常", e);
        }

    }

}
