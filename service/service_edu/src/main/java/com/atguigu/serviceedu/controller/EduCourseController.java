package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.vo.CourseInfoForm;
import com.atguigu.serviceedu.entity.vo.CoursePublishVo;
import com.atguigu.serviceedu.entity.vo.CourseQuery;
import com.atguigu.serviceedu.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-10
 */
@Api(description = "课程管理添加")
@RestController
@RequestMapping("/serviceedu/educourse")
@CrossOrigin
public class EduCourseController {

    @Autowired
    EduCourseService courseService;

    @ApiOperation(value = "添加课程")
    @PostMapping("addcourse")
    @CrossOrigin
    public R addCourseInfo(@RequestBody CourseInfoForm courseInfoForm){
        String courseId = courseService.addCourseInfo(courseInfoForm);
        if(!StringUtils.isEmpty(courseId)){
            return R.ok().data("courseId",courseId);
        }else{
            return R.error().message("保存失败");
        }
    }

    @ApiOperation(value = "根据id查询课程信息")
    @GetMapping("{courseId}")
    @CrossOrigin
    public R getCourseInfoId(@PathVariable String courseId){
        CourseInfoForm  courseInfoForm = courseService.getCourseInfo(courseId);
        return R.ok().data("courseInfo",courseInfoForm);
    }

    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourse")
    public R updateCourse(@RequestBody CourseInfoForm courseInfoForm){
        courseService.updateCourse(courseInfoForm);
        return R.ok();
    }
    @ApiOperation(value = "根据id查询发布课程信息")
    @GetMapping("getCoursePublishVoById/{courseId}")
    public R getCoursePublishVoById(@PathVariable String courseId){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVoById(courseId);
        return R.ok().data("coursePublishVo",coursePublishVo);
    }

    @ApiOperation(value = "发布课程信息")
    @PostMapping("coursePublish/{id}")
    public R coursePublish(@PathVariable String id){
        //把status字段改为Normal
        //根据id查询课程信息
        EduCourse eduCourse = courseService.getById(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }


    @ApiOperation(value = "分页课程列表")
    @GetMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    CourseQuery courseQuery){

        Page<EduCourse> pageParam = new Page<>(page, limit);

        courseService.pageQuery(pageParam, courseQuery);
        List<EduCourse> records = pageParam.getRecords();

        long total = pageParam.getTotal();

        return  R.ok().data("total", total).data("rows", records);
    }


    @ApiOperation(value = "删除课程以及相关信息")
    @DeleteMapping("{courseId}")
    public R deleteCourse(@PathVariable String courseId){

        courseService.deleteCourse(courseId);
        return R.ok();
    }

}

