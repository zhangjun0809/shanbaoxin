package com.atguigu.serviceedu.client;

import com.atguigu.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author sbxstrator
 * @create 2020-02-12 23:14
 */
@FeignClient(value = "service-vod",fallback = VodFileDegradeFeignClient.class)//服务名
@Component
public interface VodClient {
    //定义操作方法
    //url必须是完整的
    //@PathVariable后面必须添加注解

    @DeleteMapping("/eduvod/filevod/{videoId}")
    public R deleteVideo(@PathVariable String videoId);

    @DeleteMapping("/eduvod/filevod/deleteMoreVideo")
    public R deleteMoreVideo(@RequestParam List list);
}
