package com.atguigu.cmsservice.controller;


import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.atguigu.commonutils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-02-15
 */
@RestController
@RequestMapping("/cmsservice/banner")
@CrossOrigin
@Api(description = "banner操作")
public class CrmBannerController {

    @Autowired
    private CrmBannerService bannerService;


    @ApiOperation(value = "分页查询")
    @GetMapping("getBannerList/{current}/{limit}")
    public R getBannerList(@PathVariable long current,@PathVariable long limit){
        Page<CrmBanner> page = new Page<>(current, limit);
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        //查询所有分页信息，存入到page里
        bannerService.page(page,wrapper);
        return R.ok().data("total",page.getTotal()).data("rows",page.getRecords());
    }

    @ApiOperation(value = "添加banner")
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        bannerService.save(crmBanner);
        return R.ok();
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("{id}")
    public R getBannerById(@PathVariable String id){
        CrmBanner crmBanner = bannerService.getById(id);

        return R.ok().data("banner",crmBanner);
    }


    @ApiOperation(value = "修改banner")
    @GetMapping("updateBanenr")
    public R updateBanenr(@PathVariable String id){
        boolean b =bannerService.removeById(id);
        return R.ok();
    }



}

