package com.atguigu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author sbxstrator
 * @create 2020-02-17 16:07
 */
@Service
public class serviceImpl implements MsmService {

    //根据手机号发送验证码
    @Override
    public boolean sendMsm(String phone, HashMap<String, String> map) {
        //根据手机号发送验证码
        DefaultProfile profile =
                DefaultProfile.getProfile("default", "LTAI4FsaxE5xF71qYjVgFhty", "Hm4lGcMMxzgrC9y4fpEMNiW77TRexh");
        IAcsClient client = new DefaultAcsClient(profile);
        //2创建request对象
        CommonRequest request = new CommonRequest();

        //3.向request里面设置对象
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "谷里学院在线教育");
        request.putQueryParameter("TemplateCode", "SMS_183791340");
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));



        try {
            //4.初始化对象调用方法
            CommonResponse response = client.getCommonResponse(request);
            //5.通过response获取是否发送成功
            boolean success = response.getHttpResponse().isSuccess();
            return  success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }
}
