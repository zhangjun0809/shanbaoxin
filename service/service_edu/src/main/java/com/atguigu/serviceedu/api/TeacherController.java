package com.atguigu.serviceedu.api;

import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduCourse;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.service.EduCourseService;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author sbxstrator
 * @create 2020-02-19 15:19
 */
@RestController
@RequestMapping("/serviceedu/frontteacher")
@Api(description = "前台讲师列表展示")
@CrossOrigin
public class TeacherController {


    @Autowired
    EduTeacherService teacherService;

    @Autowired
    EduCourseService courseService;

    @ApiOperation(value = "前台讲师的分页查询功能")
    @GetMapping("getFrontTeacherList/{current}/{limit}")
    public R pageList(@PathVariable long current,@PathVariable long limit){

        Page<EduTeacher> pageParam = new Page<>(current,limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("gmt_create");
        teacherService.page(pageParam,wrapper);

        List<EduTeacher> records = pageParam.getRecords();
        long currentPage = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long total = pageParam.getTotal();
        long size = pageParam.getSize();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();
        //分页数据放到map里
        HashMap<String, Object> map = new HashMap<>();
        map.put("items",records);
        map.put("current",currentPage);
        map.put("pages",pages);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);

        return R.ok().data(map);

    }
    @ApiOperation(value = "根据id查询讲师信息")
    @GetMapping("getTeacherInfo/{id}")
    public R getTeacherInfo(@PathVariable String id){
        //1根据id查询讲师基本信息
        EduTeacher eduTeacher = teacherService.getById(id);
        //2查询讲师所讲的课程信息
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("teacher_id",id);
        List<EduCourse> list = courseService.list(wrapper);
        return R.ok().data("eduTeacher",eduTeacher).data("courseList",list);
    }


}
