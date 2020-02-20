package com.atguigu.vodservice.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author sbxstrator
 * @create 2020-02-12 23:14
 */
public interface VideoService {
    String uploadVideo(MultipartFile file);

    void removeVideo(String videoId);

    void deleteMoreVideo(String join);
}
