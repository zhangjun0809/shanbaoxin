package com.atguigu.ossservice.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.atguigu.ossservice.service.FileService;
import com.atguigu.ossservice.utils.ConstantPropertiesUtil;
import com.atguigu.servicebase.exception.GuliException;
import lombok.SneakyThrows;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.ConstantCallSite;
import java.util.UUID;

/**
 * @author sbxstrator
 * @create 2020-02-07 15:15
 */
@Service
public class FileServiceImpl implements FileService {


    @SneakyThrows
    @Override
    public String uploadFileOss(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtil.END_POINT;
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;



// 创建OSSClient实例。
        try {
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
            InputStream inputStream = file.getInputStream();
            String filename = file.getOriginalFilename();
            //uuid
            String uuid = UUID.randomUUID().toString();
            //拼接文件名
            filename = uuid+ filename;
            String path = new DateTime().toString("yyy/MM/dd");
            filename = path+"/"+filename;
            ossClient.putObject(bucketName, filename, inputStream);
// 关闭OSSClient。
            ossClient.shutdown();

            String url = "https://"+bucketName+"."+endpoint+"/"+filename;

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(2001,"上传失败");
        }
    }
}
