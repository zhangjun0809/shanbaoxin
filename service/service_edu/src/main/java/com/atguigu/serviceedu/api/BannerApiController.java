package com.atguigu.serviceedu.api;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sbxstrator
 * @create 2020-02-17 9:27
 */
@RestController
@RequestMapping("/serviceedu/index")
@Api(description = "章节管理")
@CrossOrigin
public class BannerApiController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;


    @ApiOperation(value = "首页展示8条课程、4个名师")
    @GetMapping
    public R getIndexData(){

        //1.查询8条课程
        QueryWrapper<EduCourse> wrapperCourse = new QueryWrapper<>();
        //1.1根据id降序排
        wrapperCourse.orderByDesc("id");
        //1.2查询8条记录
        wrapperCourse.last("limit 8");
        List<EduCourse> listCourse = courseService.list(wrapperCourse);


        //2.查询四个名师
        QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
        //2.根据id降序排
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> listTeacher = teacherService.list(wrapperTeacher);
        return R.ok().data("hotCourse",listCourse).data("hotTeacher",listTeacher);
    }
}
