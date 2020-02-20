package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-17
 */
@RestController
@RequestMapping("/ucenterservice/ucentermember")
@Api(description = "前端登录注册管理")
@CrossOrigin
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;



    @ApiOperation(value = "注册方法")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo member){
        System.out.println("进入到注册方法");
        memberService.register(member);
        return  R.ok();
    }

    @ApiOperation(value = "登录方法")
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member){
        //返回一个token字符串,包含用户信息
      String token = memberService.login(member);
        return R.ok().data("token",token);
    }


    //根据token字符串获取用户信息
    @ApiOperation(value = "根据token字符串获取用户信息")
    @PostMapping("getInfoToken")
    public R getInfoToken(HttpServletRequest request){
        //根据JWT获取用户信息
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //根据用户id或缺的用户信
        UcenterMember ucenterMember = memberService.getById(memberId);
        return R.ok().data("member",ucenterMember);

    }
}

