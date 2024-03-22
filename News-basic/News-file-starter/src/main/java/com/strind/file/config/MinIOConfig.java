package com.strind.file.config;

import com.strind.file.service.FileStorageService;
import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author strind
 * @version 1.0
 * @description minio 配置
 * @date 2024/3/22 15:18
 */
@Data
@Configuration
@EnableConfigurationProperties(MinIOConfigProperties.class)
@ConditionalOnClass(FileStorageService.class)
public class MinIOConfig {
    @Autowired
    private MinIOConfigProperties minIOConfigProperties;

    @Bean
    public MinioClient buildMinioClient(){
        return MinioClient.builder()
            .credentials(minIOConfigProperties.getAccessKey(), minIOConfigProperties.getSecretKey())
            .endpoint(minIOConfigProperties.getEndpoint())
            .build();
    }

}
