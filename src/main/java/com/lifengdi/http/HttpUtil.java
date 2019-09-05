package com.lifengdi.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lifengdi.commons.utils.Either;
import com.lifengdi.exception.ApiException;
import com.lifengdi.exception.BaseException;
import com.lifengdi.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * http工具类
 *
 * @author 李锋镝
 * @date Create at 10:59 2019/5/10
 */
@Component
@Slf4j
public class HttpUtil {

    @Resource
    private RestTemplate restTemplate;

    /**
     * GET 请求
     * @param url 请求地址
     * @param params 请求参数
     * @return Either
     */
    public  Either<ApiException, ResponseResult> get(String url, Map<String, Object> params) {
        if (!url.startsWith("http")) {
            return Either.left(BaseException.URL_FORMAT_ERROR.build());
        }
        if (!CollectionUtils.isEmpty(params)) {
            StringJoiner stringJoiner = new StringJoiner("&");
            for (Map.Entry entry : params.entrySet()) {
                stringJoiner.add(entry.getKey() + "=" + entry.getValue());
            }
            if (url.endsWith("?")) {
                // eg.: http://www.example.com/api?
                url = url + stringJoiner.toString();
            } else if (url.indexOf("?") > 0 && !url.endsWith("?")) {
                if (url.endsWith("&")) {
                    // eg.: http://www.example.com/api?key=value&
                    url = url + stringJoiner.toString();
                } else {
                    // eg.: http://www.example.com/api?key=value
                    url = url + "&" + stringJoiner.toString();
                }
            } else {
                // eg.: http://www.example.com/api
                url = url + "?" + stringJoiner.toString();
            }
        }
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity requestEntity = new HttpEntity(requestHeaders);
        ParameterizedTypeReference typeRef = new ParameterizedTypeReference<ResponseResult<Object>>(){};

        log.info("发送GET请求，URL：{}，参数：{}", url, params);
        final ResponseEntity<ResponseResult> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, typeRef);
        log.info("发送GET请求，URL:{}，参数:{}，接口响应内容:{}", url, params, response.getBody());
        if (response.getStatusCode().is2xxSuccessful()) {
            return Either.right(response.getBody());
        }

        return Either.left(BaseException.GET_REQUEST_EXCEPTION.build());
    }

    /**
     * POST 请求
     * @param url URL
     * @param params 请求参数
     * @return Either
     */
    public Either<ApiException, ResponseResult> post(String url, JSONObject params) {
        if (!url.startsWith("http")) {
            return Either.left(BaseException.URL_FORMAT_ERROR.build());
        }
        ParameterizedTypeReference typeReference = new ParameterizedTypeReference<ResponseResult<Object>>(){};
        HttpHeaders requestHeaders = new HttpHeaders();
        if (Objects.isNull(params)) {
            params = new JSONObject();
        }
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(params, requestHeaders);
        log.info("发送POST请求，URL：{}，参数：{}", url, params);
        final ResponseEntity<ResponseResult> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, typeReference);
        log.info("发送POST请求，URL:{}，参数:{}，接口响应内容:{}", url, params, response.getBody());
        if (response.getStatusCode().is2xxSuccessful()) {
            return Either.right(response.getBody());
        }

        return Either.left(BaseException.POST_REQUEST_EXCEPTION.build());
    }

    /**
     * POST 请求
     * @param url URL
     * @param params 请求参数
     * @return Either
     */
    public Either<ApiException, ResponseResult> post(String url, Map<String, Object> params) {
        if (CollectionUtils.isEmpty(params)) {
            return post(url, null);
        }
        return post(url, JSON.parseObject(JSON.toJSONString(params)));
    }

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
