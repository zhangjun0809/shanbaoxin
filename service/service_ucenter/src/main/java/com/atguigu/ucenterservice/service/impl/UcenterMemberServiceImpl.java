package com.atguigu.ucenterservice.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.servicebase.exception.GuliException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-02-17
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    //注册方法实现
    @Override
    public void register(RegisterVo member) {
        String nickname = member.getNickname();
        String mobile = member.getMobile();
        String password = member.getPassword();
        String code = member.getCode();

        //1判断参数是否为空
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(nickname)||
                StringUtils.isEmpty(password)||StringUtils.isEmpty(code)){
            throw new GuliException(20001,"参数为空");
        }

        //2根据手机号查询是否存在相同手机号
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if(count>0){
            throw new GuliException(20001,"手机号已存在");
        }
        //3判断验证码
        String codeRedis = redisTemplate.opsForValue().get(mobile);
        if(!code.equals(codeRedis)){
            throw new GuliException(20001,"验证码错误");
        }

        //4存入数据库
        //4.1复制数据
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(member,ucenterMember);

        //4.2密码加密
        String passwordBefore = ucenterMember.getPassword();
        String passwordafter = MD5.encrypt(passwordBefore);
        ucenterMember.setPassword(passwordafter);

        //4.3手动设置一些值
        ucenterMember.setIsDisabled(false);
        ucenterMember.setAvatar("https://guli-file190919test.oss-cn-beijing.aliyuncs.com/2020/02/07/15e2e030-0115-489d-969e-e8590ff48693file.png");

        int insert = baseMapper.insert(ucenterMember);

        if(insert==0){
            throw new GuliException(20001,"注册失败");
        }
    }

    //登录方法
    @Override
    public String login(UcenterMember member) {
        String mobile = member.getMobile();
        String password = member.getPassword();

        //1.判断手机号、密码是否为空
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            throw new GuliException(2001,"登录失败,请检查手机号或密码");
        }
        //2根据手机号查询进行验证
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if(ucenterMember==null){
            throw  new GuliException(2001,"登录失败,请检查手机号或密码");
        }
        //3.判断密码
        String passwordDatabase = ucenterMember.getPassword();
        //3.1输入密码需要进行MD5加密
        String pass = MD5.encrypt(password);
        //数据库密码和加密后的密码进行判断
        if(!pass.equals(passwordDatabase)){
            throw new GuliException(2001,"登录失败,请检查手机号或密码");
        }

        //4判断用户是否被禁用
        if(ucenterMember.getIsDisabled()){
            throw new GuliException(2001,"账号已被冻结");
        }

        String jwtToken = JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());


        return jwtToken;
    }
}
