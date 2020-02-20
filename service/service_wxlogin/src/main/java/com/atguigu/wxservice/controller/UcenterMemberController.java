package com.atguigu.wxservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.wxservice.entity.UcenterMember;
import com.atguigu.wxservice.service.UcenterMemberService;
import com.atguigu.wxservice.util.ConstantPropertiesUtil;
import com.atguigu.wxservice.util.HttpClientUtils;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-18
 */
@Api(description = "微信三方服务模块")
@Controller
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class UcenterMemberController {
    @Autowired
    UcenterMemberService memberService;

    @ApiOperation(value = "生成微信登录二维码方法")
    @GetMapping("login")
    public String login() {
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String wxOpenRedirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;
        try {
            URLEncoder.encode(wxOpenRedirectUrl,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //向%s传递参数
        String wxUrl = String.format(baseUrl, ConstantPropertiesUtil.WX_OPEN_APP_ID, wxOpenRedirectUrl, "wxstguigu");

        //重定向
        return "redirect:"+wxUrl;
    }
    @ApiOperation(value = "微信扫码后回调")
    @GetMapping("callback")
    public String callback(String code,String state){

        //1.拿着code，请求微信固定地址,得到微信id和许可
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        String format = String.format(baseAccessTokenUrl, ConstantPropertiesUtil.WX_OPEN_APP_ID
                , ConstantPropertiesUtil.WX_OPEN_APP_SECRET
                , code);


        try {
            String accessTokenInfo = HttpClientUtils.get(format);
            //System.out.println("accessTokenInfo"+accessTokenInfo);
            Gson gson = new Gson();
            HashMap accessToeknMap = gson.fromJson(accessTokenInfo, HashMap.class);
            String openid = (String) accessToeknMap.get("openid");
            String access_token = (String) accessToeknMap.get("access_token");

            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String format1 = String.format(baseUserInfoUrl, access_token, openid);
            //HttpClient请求地址
            String userInfo = HttpClientUtils.get(format1);
            System.out.println("----------------userInfo="+userInfo);
            HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
            String nickname = (String) userInfoMap.get("nickname");
            String headimgurl = (String) userInfoMap.get("headimgurl");

            //根据openid查询用户
            UcenterMember member = memberService.getWxInfoById(openid);
            if(member==null){//没有相同的微信，进行添加操作
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }
            //生成token,返回
            String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //返回首页
            return "redirect:http://localhost:3000?token="+token;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(20001,"扫码失败");
        }
    }

}

