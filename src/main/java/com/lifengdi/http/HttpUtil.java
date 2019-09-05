package com.lifengdi.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http工具类
 *
 * @author 李锋镝
 * @date Create at 10:59 2019/5/10
 */
@Component
@Slf4j
public class HttpUtil {

    /**
     * 下载文件
     * @param fileUrl 文件URL
     * @param fileName 文件名
     * @param saveFolder 文件保存目录
     * @return 文件路径
     * @throws IOException IOException
     */
    public static String downloadFile(String fileUrl, String fileName, String saveFolder) throws IOException {
        String pathName = null;
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置超时间
            connection.setConnectTimeout(5 * 1000);
            // 防止屏蔽程序抓取而返回403错误
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            if (connection.getResponseCode() == HttpStatus.OK.value()) {
                inputStream = connection.getInputStream();
                // 获取数组
                byte[] getData = readInputStream(inputStream);
                // 文件保存位置
                File saveDir = new File(saveFolder);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                pathName = saveDir + File.separator + fileName;
                File file = new File(pathName);
                fos = new FileOutputStream(file);
                fos.write(getData);
            }
        } catch (IOException e) {
            log.error("下载文件异常", e);
            throw e;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("FileOutputStream关闭异常", e);
            }
        }
        return pathName;
    }

    /**
     * 读取字节流
     * @param inputStream inputStream
     * @return byte[]
     * @throws IOException IOException
     */
    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void main(String[] args) {
        try {
            System.out.println(downloadFile("http://www.lifengdi.com", "2396.txt", "/Users/goddess/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
