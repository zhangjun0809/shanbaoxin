package com.atguigu.vodservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.vodservice.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @author sbxstrator
 * @create 2020-02-12 23:18
 */
@Api(description = "上传文件管理")
@RestController
@RequestMapping("/eduvod/filevod")
@CrossOrigin
public class VideoAdminController {

    @Autowired
    private VideoService videoService;


    //上传视频文件方法
    @ApiOperation(value = "上传视频文件")
    @PostMapping("upload")
    public R uploadVideo(@RequestParam("file") MultipartFile file) throws Exception{
        String videoId = videoService.uploadVideo(file);
        return R.ok().message("视频上传成功").data("videoId",videoId);
    }


    @ApiOperation(value = "删除云端视频文件")
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        videoService.removeVideo(videoId);
        return R.ok();
    }


    @ApiOperation(value = "删除云端多个视频文件")
    @DeleteMapping("deleteMoreVideo")
    public R deleteMoreVideo(@RequestParam List list){
        String join = StringUtils.join(list, ",");

        videoService.deleteMoreVideo(join);

        return R.ok();

    }
}
