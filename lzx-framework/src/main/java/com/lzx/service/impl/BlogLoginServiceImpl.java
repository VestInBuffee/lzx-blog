package com.lzx.service.impl;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.LoginUser;
import com.lzx.domain.entity.User;
import com.lzx.domain.vo.BlogLoginVo;
import com.lzx.domain.vo.UserInfoVo;
import com.lzx.service.BlogLoginService;

import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.JwtUtil;
import com.lzx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //1.首先进行身份认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        /*
         * 1.1在authenticate方法中，
         * 会调用UserDetailsService的方法，该方法进行查询user，并返回UserDetails对象
         */
        Authentication authentication =
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        //2.认证是否成功？进行判断
        if(Objects.isNull(authentication)){
            throw new RuntimeException("信息错误");
        }

        //3.认证成功后，进行jwt
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtil.createJWT(userId);

        //存入redis
        redisCache.setCacheObject("bloglogin:"+userId, loginUser);

        //返回结果
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        return ResponseResult.okResult(new BlogLoginVo(token, userInfoVo));
    }

    @Override
    public ResponseResult logout() {
        //1.从context获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) (authentication.getPrincipal());
        String userId = loginUser.getUser().getId().toString();
        //2.从redis中删除该userId的数据
        redisCache.deleteObject("bloglogin:" + userId);
        return ResponseResult.okResult("登出成功");
    }
}
