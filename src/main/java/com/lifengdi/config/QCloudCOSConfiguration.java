package com.lifengdi.config;

import com.lifengdi.util.GeneratedKey;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 腾讯云上传文件
 * @author 李锋镝
 * @date Create at 16:28 2018/7/5
 * @modified by
 */
@Data
@Configuration
public class QCloudCOSConfiguration {

    @Value("${cloud.app.id}")
    private long appId;

    @Value("${cloud.secret.id}")
    private String secretId;

    @Value("${cloud.secret.key}")
    private String secretKey;

    @Value("${cloud.bucket.name}")
    private String bucket_name;

    @Value("${cloud.cdn.dir.path}")
    private String cdn_dir_path;

    @Value("${cloud.cdn.url}")
    private String cdn_url;

    @Value("${cloud.region}")
    private String regionName;

    @Value("${file.localTempPath}")
    private String localTempPath;

    @Resource
    private GeneratedKey generatedKey;

    /**
     * 上传文件到腾讯云
     * @param fileName 文件名
     * @return The key under which to store the new object.
     */
    public String putObject(String fileName) {
        COSClient cosClient = null;
        try {
            cosClient = initCOSClient();

            String bucketName = bucket_name + "-" + appId;
            // 文件操作 上传文件(将本地文件上传到COS) 将本地的fileName上传到bucket下的根分区下 默认不覆盖, 如果cos上已有文件, 则返回错误
            String first = fileName;
            if (!fileName.startsWith(localTempPath)) {
                first = localTempPath + fileName;
            }
            Path filePath = Paths.get(first);
            String key;
            String cdnDirPath = getCdn_dir_path();
            DateTime dateTime = new DateTime();
            int year = dateTime.getYear();
            int month = dateTime.getMonthOfYear();
            cdnDirPath = cdnDirPath.replaceFirst("yyyy", String.valueOf(year));
            cdnDirPath = cdnDirPath.replaceFirst("MM", String.valueOf(month));
            if (cdnDirPath.endsWith("/")) {
                key = cdnDirPath + fileName;
            } else {
                key = cdnDirPath + "/" + fileName;
            }
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, filePath.toFile());
            cosClient.putObject(putObjectRequest);
            return key;
        } finally {
            if (Objects.nonNull(cosClient)) {
                // 关闭客户端(关闭后台线程)
                cosClient.shutdown();
            }
        }
    }

    /**
     * 初始化COS客户端
     * @return COSClient
     */
    private COSClient initCOSClient() {
        // 设置bucket所在的区域
        Region region = new Region(regionName);
        // 初始化客户端配置
        ClientConfig clientConfig = new ClientConfig(region);
        // 初始化秘钥信息
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 初始化cosClient
        return new COSClient(cred, clientConfig);
    }

    /**
     * 下载腾讯云文件
     * @param fileName 文件名
     * @param cloudPath 文件在腾讯云存储桶中的路径
     * @return File
     */
    public File loadObject(String fileName, String cloudPath) {
        File downFile = new File(localTempPath + fileName);
        String bucketName = bucket_name + "-" + appId;
        COSClient cosClient = null;
        try {
            cosClient = initCOSClient();
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, cloudPath);
            ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
            if (Objects.nonNull(downObjectMeta)) {
                return downFile;
            }
        } finally {
            if (Objects.nonNull(cosClient)) {
                // 关闭客户端(关闭后台线程)
                cosClient.shutdown();
            }
        }
        return null;
    }

    /**
     * 下载腾讯云文件
     * @param cloudPath 文件在腾讯云存储桶中的路径
     * @return File
     */
    public File loadObject(String cloudPath) {
        String fileName = generatedKey.generatorKey() + cloudPath.substring(cloudPath.lastIndexOf("."), cloudPath.length());
        return loadObject(fileName, cloudPath);
    }

    /**
     * 根据文件在腾讯云的key获取文件的完整路径
     * @param key 文件在腾讯云的key(The key under which to store the new object.)
     * @return 文件的完整路径
     */
    public String qCloudUrl(String key) {
        if (key.startsWith("/")) {
            key = key.substring(1, key.length());
        }
        String cdnUrl = getCdn_url();
        if (cdnUrl.endsWith("/")) {
            key = cdnUrl + key;
        } else {
            key = cdnUrl + "/" + key;
        }
        return key;
    }

}
