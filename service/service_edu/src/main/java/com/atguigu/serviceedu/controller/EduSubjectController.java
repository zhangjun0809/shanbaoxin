package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.vo.OneSubjectVo;
import com.atguigu.serviceedu.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-08
 */
@RestController
@RequestMapping("/serviceedu/edusubject")
@Api(description="课程管理")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;

    @ApiOperation(value = "添加课程分类")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        List<String> msgList = eduSubjectService.importSubjectData(file);
        //判断是否有数据
        System.out.println(msgList.size());
        if(msgList.size()==0){
            //成功
            return R.ok();
        }else{
            return R.error().data("msgList",msgList);
        }
    }

    @ApiOperation(value = "查询课程分类")
    @GetMapping
    public R getAllSubject(){
        List<OneSubjectVo> allSubject = eduSubjectService.getAllSubject();
        return R.ok().data("allSubject",allSubject);
    }
}


