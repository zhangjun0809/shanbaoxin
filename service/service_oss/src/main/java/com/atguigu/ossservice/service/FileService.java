package com.atguigu.ossservice.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author sbxstrator
 * @create 2020-02-07 15:15
 */
public interface FileService {

    String uploadFileOss(MultipartFile file);
}
