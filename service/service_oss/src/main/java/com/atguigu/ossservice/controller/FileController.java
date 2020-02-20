package com.atguigu.ossservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.ossservice.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author sbxstrator
 * @create 2020-02-07 15:12
 */
@Api(description = "上传文件管理")
@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin
public class FileController {

    @Autowired
    FileService fileService;


    @ApiOperation(value = "上传文件")
    @PostMapping("fileUpload")
    public R fileUploadOss(MultipartFile file){
        //1.得到上传过来的文件


        //2.获取到的问价上传到阿里云
        String url = fileService.uploadFileOss(file);

        //3.返回OSS地址
        return R.ok().data("url",url);

    }

}
