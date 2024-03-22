package com.strind.file.service;

import java.io.InputStream;

/**
 * @author strind
 * @version 1.0
 * @description 文件存储接口规范
 * @date 2024/3/22 15:19
 */
public interface FileStorageService {

    /**
     * 上传图片文件
     * @param prefix 文件前缀
     * @param fileName 文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    String uploadImgFile(String prefix, String fileName, InputStream inputStream);

    /**
     * 上传html文件
     * @param prefix 文件前缀
     * @param fileName 文件名
     * @param inputStream 文件流
     * @return 文件全路径
     */
    String uploadHtmlFile(String prefix, String fileName, InputStream inputStream);

    /**
     * 删除文件
     * @param pathUrl 文件全路径
     */
    void delete(String pathUrl);

    /**
     * 下载文件
     * @param pathUrl 文件全路径
     * @return 文件
     */
    byte[] downLoadFile(String pathUrl);

}
