package com.strind.file.service.impl;

import com.strind.file.config.MinIOConfig;
import com.strind.file.config.MinIOConfigProperties;
import com.strind.file.service.FileStorageService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author strind
 * @version 1.0
 * @description minio实现文件存储
 * @date 2024/3/22 15:19
 */
@Slf4j
@EnableConfigurationProperties(MinIOConfigProperties.class)
@Import(MinIOConfig.class)
public class MinIOFileStorageService implements FileStorageService {

    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    @Autowired
    private MinioClient minioClient;

    private static final String separator = "/";

    /**
     * 上传图片文件
     *
     * @param prefix      文件前缀
     * @param fileName    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    @Override
    public String uploadImgFile(String prefix, String fileName, InputStream inputStream) {
        String filePath = buildFilePath(prefix, fileName);
        try {
            PutObjectArgs putArgs = PutObjectArgs.builder()
                .object(filePath)
                .contentType("image/jpg")
                .bucket(minIOConfigProperties.getBucket())
                .stream(inputStream,inputStream.available(), -1)
                .build();
            minioClient.putObject(putArgs);
            StringBuilder urlPath = new StringBuilder(minIOConfigProperties.getReadPath());
            urlPath.append(separator + minIOConfigProperties.getBucket());
            urlPath.append(separator + filePath);
            return urlPath.toString();
        }catch (Exception e){
            log.error("minio put file error", e);
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     * 上传html文件
     *
     * @param prefix      文件前缀
     * @param fileName    文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    @Override
    public String uploadHtmlFile(String prefix, String fileName, InputStream inputStream) {
        String filePath = buildFilePath(prefix, fileName);
        try {
            PutObjectArgs putArgs = PutObjectArgs.builder()
                .object(filePath)
                .contentType("text/html")
                .bucket(minIOConfigProperties.getBucket())
                .stream(inputStream,inputStream.available(), -1)
                .build();
            minioClient.putObject(putArgs);
            StringBuilder urlPath = new StringBuilder(minIOConfigProperties.getReadPath());
            urlPath.append(separator + minIOConfigProperties.getBucket());
            urlPath.append(separator + filePath);
            return urlPath.toString();
        }catch (Exception e){
            log.error("minio put file error", e);
            throw new RuntimeException("上传文件失败");
        }
    }

    /**
     * 删除文件
     *
     * @param pathUrl 文件全路径
     */
    @Override
    public void delete(String pathUrl) {
        String key = pathUrl.replace(minIOConfigProperties.getEndpoint() + separator, "");
        int index = key.indexOf(separator);
        String bucket = key.substring(0, index);
        String filePath = key.substring(index + 1);
        try {
            RemoveObjectArgs removeArges = RemoveObjectArgs
                .builder().bucket(bucket).object(filePath).build();
            minioClient.removeObject(removeArges);
        }catch (Exception e){
            log.error("minio remove file error, pathUrl: {}",pathUrl);
        }
    }

    /**
     * 下载文件
     *
     * @param pathUrl 文件全路径
     * @return 文件
     */
    @Override
    public byte[] downLoadFile(String pathUrl) {
        String key = pathUrl.replace(minIOConfigProperties.getEndpoint() + separator, "");
        int index = key.indexOf(separator);
        String bucket = key.substring(0, index);
        String filePath = key.substring(index + 1);
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(filePath).build());
        }catch (Exception e){
            log.error("minio down file error, pathUrl: {}",pathUrl);
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int rc = 0;
        while (true){
            try {
                if ((rc = inputStream.read(buffer,0,1024)) > 0)break;
            }catch (IOException e){
                e.printStackTrace();
            }
            byteArrayOutputStream.write(buffer,0,rc);
        }
        return byteArrayOutputStream.toByteArray();
    }


    private String buildFilePath(String dirPath, String fileName){
        StringBuilder stringBuilder = new StringBuilder(50);
        if (!StringUtils.isEmpty(dirPath)){
            stringBuilder.append(dirPath).append(separator);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String todayStr = sdf.format(new Date());
        stringBuilder.append(todayStr).append(separator);
        stringBuilder.append(fileName);
        return stringBuilder.toString();
    }
}
