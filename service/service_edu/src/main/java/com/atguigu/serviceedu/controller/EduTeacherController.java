package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduTeacher;
import com.atguigu.serviceedu.entity.vo.TeacherQuery;
import com.atguigu.serviceedu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-01-13
 */
@RestController
@RequestMapping("/serviceedu/eduteacher")
@Api(description="讲师管理")
@CrossOrigin
public class EduTeacherController {
    @Autowired
    EduTeacherService teacherService;


    @ApiOperation(value = "根据id删除")
    @DeleteMapping("{id}")
    public R deleteTeacherId(@PathVariable String id){
        boolean remove = teacherService.removeById(id);
        return R.ok() ;
    }


    @ApiOperation(value = "所有讲师列表")
    @GetMapping("getTeacherPage")
    public R getAllTeacher(){
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items",list);
    }

    @ApiOperation(value = "分页查询讲师")
    @GetMapping("getTeacherPage/{current}/{limit}")
    public R getTeacherPage(@PathVariable Long current,@PathVariable Long limit){
        //1.创建page对象
        Page<EduTeacher> page = new Page<>(current, limit);
        //2.调方法获取数据
        teacherService.page(page,null);
        //3.从page中获取数据
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        return  R.ok().data("total",total).data("items",records);
    }

    @ApiOperation(value = "分页查询讲师")
    @PostMapping("getTeacherPageVo/{current}/{limit}")
    @CrossOrigin
    public R getTeacherPageVo(@PathVariable Long current, @PathVariable Long limit, @RequestBody TeacherQuery teacherQuery){
        //创建page对象
        Page<EduTeacher> page = new Page<>(current, limit);
        //取出参数
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)) {
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }
        teacherService.page(page,wrapper);
        long total = page.getTotal();
        List<EduTeacher> records = page.getRecords();
        return  R.ok().data("total",total).data("items",records);
    }

    //实现添加讲师功能
    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else{
            return R.error();
        }
    }

    @ApiOperation(value = "根据ID查询讲师")
    @GetMapping("getTeacherById/{id}")
    public R getTeacherById(@PathVariable String id){
        EduTeacher eduTeacher = teacherService.getById(id);
        return  R.ok().data("eduTeacher",eduTeacher);
    }


    @ApiOperation(value = "修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean update = teacherService.updateById(eduTeacher);
        if (update) {
            return R.ok();
        } else {
            return R.error();
        }
    }


}

