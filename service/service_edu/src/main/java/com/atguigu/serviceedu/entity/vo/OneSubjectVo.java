package com.atguigu.serviceedu.entity.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sbxstrator
 * @create 2020-02-08 22:35
 */
@Data
public class OneSubjectVo {
    private String id;
    private String title;
    private List<TwoSubjectVo> children = new ArrayList<>();
}
