package com.atguigu.msmservice.service;

import java.util.HashMap;

/**
 * @author sbxstrator
 * @create 2020-02-17 16:07
 */
public interface MsmService {
    boolean sendMsm(String phone, HashMap<String, String> map);
}
