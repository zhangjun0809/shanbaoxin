package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.client.VodClient;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-11
 */
@RestController
@RequestMapping("/serviceedu/video")
@Api(description = "小节管理")
@CrossOrigin
public class EduVideoController {

    @Autowired
    EduVideoService videoService;

    @Autowired
    VodClient vodClient;

    @ApiOperation(value = "添加小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        videoService.save(eduVideo);
        return R.ok();
    }

    @ApiOperation(value = "删除小节")
    @DeleteMapping("{id}")
    //TODO
    public R deleteVideo(@PathVariable String id){
        //根据小节id查询视频id
        EduVideo eduVideo = videoService.getById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)){
            vodClient.deleteVideo(videoSourceId);
        }
        videoService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据id查询小节")
    @GetMapping("{id}")
    public R getVideoInfo(@PathVariable String id){
        EduVideo eduVideo = videoService.getById(id);
        return  R.ok().data("video",eduVideo);
    }

    @ApiOperation(value = "根据id修改小节")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){


       videoService.updateById(eduVideo);
        return  R.ok();
    }

}

