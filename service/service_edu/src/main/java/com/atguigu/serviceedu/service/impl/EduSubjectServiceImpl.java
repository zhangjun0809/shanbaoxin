package com.atguigu.serviceedu.service.impl;

import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.serviceedu.entity.EduSubject;
import com.atguigu.serviceedu.entity.vo.OneSubjectVo;
import com.atguigu.serviceedu.entity.vo.TwoSubjectVo;
import com.atguigu.serviceedu.mapper.EduSubjectMapper;
import com.atguigu.serviceedu.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-08
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public List<String> importSubjectData(MultipartFile file) {
        try {
            //1.获取文件输入流
            InputStream in = file.getInputStream();
            //2.创建workbook
            Workbook workbook = new HSSFWorkbook(in);
            //3.获取sheet
            Sheet sheet = workbook.getSheetAt(0);
           //获取内容的行数
            int lastRowNum = sheet.getLastRowNum();
            List<String> msg = new ArrayList<>();
            //循环取值
            for (int i = 1; i < lastRowNum; i++) {
                //4.获取row
                Row row = sheet.getRow(i);
                //5.获取cell
                Cell cellOne = row.getCell(0);
                //获取第一列
                //获取值
                if(cellOne==null){
                    //错误提示
                    String error = "第"+(i+1)+"行,第1列数据为空";
                    msg.add(error);
                    continue;
                }
                String oneCellValue = cellOne.getStringCellValue();
                if(StringUtils.isEmpty(oneCellValue)){
                    //错误提示
                    String error = "第"+(i+1)+"行,第1列数据为空";
                    msg.add(error);
                    continue;
                }

                //判断
                EduSubject existOneSubject = this.existOneSubject(oneCellValue);
                if(existOneSubject==null){
                    existOneSubject = new EduSubject();
                    existOneSubject.setTitle(oneCellValue);
                    existOneSubject.setParentId("0");
                    baseMapper.insert(existOneSubject);
                }

                //获取一级id
                String pid = existOneSubject.getId();
                //获取第二列
                Cell cellTwo = row.getCell(1);
                if(cellTwo==null){
                    //错误 提示
                    String error = "第"+(i+1)+"行,第2列数据为空";
                    msg.add(error);
                    continue;
                }
                String twoCellValue = cellTwo.getStringCellValue();
                if(StringUtils.isEmpty(twoCellValue)){
                    //错误提示
                    String error = "第"+(i+1)+"行,第2列数据为空";
                    msg.add(error);
                    continue;
                }
                //添加二级分类
                //判断
                EduSubject existTwoSubject = this.existTwoSubject(twoCellValue, pid);
                if(existTwoSubject==null){
                    existTwoSubject = new EduSubject();
                    existTwoSubject.setTitle(twoCellValue);
                    existTwoSubject.setParentId(pid);
                    baseMapper.insert(existTwoSubject);
                }
            }

            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuliException(2002,"添加课程分类失败");
        }



    }

    @Override
    public List<OneSubjectVo> getAllSubject() {
        //获取所有一级分类
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
        //获取所有二级分类
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperOne.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //最终返回的List集合
        List<OneSubjectVo> finalSubjectList = new ArrayList<>();

        //3.封装一级分类
        for(int i=0;i<oneSubjectList.size();i++){
            //获取一级分裂
            EduSubject oneSubject = oneSubjectList.get(i);
            //oneSubject 转化成OneSubjectVo
            OneSubjectVo oneSubjectVo = new OneSubjectVo();
            BeanUtils.copyProperties(oneSubject,oneSubjectVo);
            //oneSubjectVo存入List
            finalSubjectList.add(oneSubjectVo);

            //创建集合用于封装二级分类
            List<TwoSubjectVo> twoVoList = new ArrayList<>();
            for(int  m = 0;m<twoSubjectList.size();m++){
                //获取每个二级分类数据
                EduSubject twoSubject = twoSubjectList.get(m);
                //判断一级分类的id等于二级分类的id
                if(oneSubject.getId().equals(twoSubject.getParentId())){
                    TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
                    BeanUtils.copyProperties(twoSubject,twoSubjectVo);
                   //放到二级分类中
                    twoVoList.add(twoSubjectVo);
                }
            }
            oneSubjectVo.setChildren(twoVoList);
        }

        return finalSubjectList;
    }

    private EduSubject existTwoSubject(String name, String pid) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        EduSubject eduSubject = baseMapper.selectOne(wrapper);
        return eduSubject;

    }

    private EduSubject existOneSubject(String name) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        EduSubject eduSubject = baseMapper.selectOne(wrapper);
        return eduSubject;
    }
}
