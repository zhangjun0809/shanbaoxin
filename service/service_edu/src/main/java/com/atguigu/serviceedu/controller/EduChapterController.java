package com.atguigu.serviceedu.controller;


import com.atguigu.commonutils.R;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.vo.ChapterVo;
import com.atguigu.serviceedu.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-11
 */
@RestController
@RequestMapping("/serviceedu/chapter")
@Api(description = "章节管理")
@CrossOrigin
public class EduChapterController {
    @Autowired
    EduChapterService chapterService;


    @ApiOperation(value = "根据课程id查询章节小节信息")
    @GetMapping("getChapterVideoById/{courseId}")
    public R getChapterVideoById(@PathVariable String courseId){
        List<ChapterVo> list = chapterService.getChapterVideoById(courseId);

        return R.ok().data("allChapterVideo",list);
    }



    @ApiOperation(value = "新增章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){

        chapterService.save(eduChapter);
        return R.ok();
    }


    @ApiOperation(value = "删除章节")
    @DeleteMapping("{id}")
    public R deleteChapterById(@PathVariable String id){
        chapterService.removeById(id);
        return R.ok();
    }

    @ApiOperation(value = "根据id查询章节")
    @GetMapping("{id}")
    public R queryChapterById(@PathVariable String id){
        EduChapter eduChapter = chapterService.getById(id);

        return R.ok().data("chapter",eduChapter);
    }

    @ApiOperation(value = "修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }

}

