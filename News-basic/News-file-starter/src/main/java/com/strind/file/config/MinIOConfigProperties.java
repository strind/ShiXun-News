package com.strind.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author strind
 * @version 1.0
 * @description
 * @date 2024/3/22 15:18
 */
@Data
@ConfigurationProperties(prefix = "minio")
public class MinIOConfigProperties implements Serializable {

    private String accessKey;

    private String secretKey;
    private String bucket;
    private String endpoint;
    private String readPath;

}
