package com.atguigu.servicebase.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sbxstrator
 * @create 2020-01-14 17:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuliException  extends RuntimeException{
    @ApiModelProperty(value = "状态码")
    private Integer code;

    private String msg;
}
