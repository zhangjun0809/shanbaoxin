package com.atguigu.serviceedu.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sbxstrator
 * @create 2020-02-15 10:46
 */
@Component
public class VodFileDegradeFeignClient implements VodClient {
    @Override
    public R deleteVideo(String videoId) {
        return R.error();
    }

    @Override
    public R deleteMoreVideo(List list) {
        return R.error();
    }
}
