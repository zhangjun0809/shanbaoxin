package com.atguigu.serviceedu.service.impl;

import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.EduChapter;
import com.atguigu.serviceedu.entity.EduVideo;
import com.atguigu.serviceedu.entity.vo.ChapterVo;
import com.atguigu.serviceedu.entity.vo.VideoVo;
import com.atguigu.serviceedu.mapper.EduChapterMapper;
import com.atguigu.serviceedu.service.EduChapterService;
import com.atguigu.serviceedu.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-11
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {


    @Autowired
    EduVideoService videoService;



    //根据课程id查询章节小节信息
    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {
        //1.获取课程的章节信息
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);
        //2.根据课程id获取课程的小节信息
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        //创建Lsit集合，封装最终数据
        List<ChapterVo> finalList = new ArrayList<>();


        //3.遍历章节list进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
            //3.1获取每一个章节信息
            EduChapter eduChapter = eduChapterList.get(i);
            //3.2eduChapter转化成ChapterVo
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalList.add(chapterVo);

            //创建list用来封装小节信息
            List<VideoVo> videoVoList = new ArrayList<>();
            //4.遍历所有小节的信息，进行封装
            for (int j = 0; j < eduVideoList.size(); j++) {
                //获取每一个小节信息
                EduVideo eduVideo = eduVideoList.get(j);
                //4.2判断小节的chapterid和章节是否相同
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }
            }
            //5.将封装好的小节信息存入到对应的章节
            chapterVo.setChildren(videoVoList);
        }
        for (int i = 0; i < finalList.size(); i++) {
            System.out.println(finalList.get(i));
        }
        return finalList;
    }


    //章节里有小节不能删除
    @Override
    public void removeChapterById(String id) {
        //查询章节里面有没有小节信息
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",id);
        int count = videoService.count(wrapper);
        if(count==0){
            baseMapper.deleteById(id);
        }else{
            throw new GuliException(2001,"不能删除");
        }
    }
}
