package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.RandomUtil;
import com.atguigu.msmservice.service.MsmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author sbxstrator
 * @create 2020-02-17 16:05
 */

@Api(description = "短信服务")
@RestController
@RequestMapping("/msmservice/msm")
@CrossOrigin
public class MsmApiController {

    //注入
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation(value = "根据手机号发送短信")
    @GetMapping("send/{phone}")
    public R sendMsmPhone(@PathVariable String phone){

        //1.从redis根据手机号获取数据
        String rphone = redisTemplate.opsForValue().get(phone);

        //2.如果能取出来数据直接返回
        if(!StringUtils.isEmpty(rphone)){
            return  R.ok();
        }

        //3.如果取不出来，调用接口发送短信
        String code = RandomUtil.getFourBitRandom();
        //3.2把生成校验码封装到map里传递
        HashMap<String, String> map = new HashMap<>();
        map.put("code",code);
        //3.3调用service方法
        boolean isSuccess = msmService.sendMsm(phone,map);
        if(isSuccess){
            //3.4发送成功把校验码放到redis
            //key:手机号  value:验证码
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        }else{
            return R.error().message("发送短信失败");
        }
    }
}
