package com.lzx.service.impl;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.LoginUser;
import com.lzx.domain.entity.User;
import com.lzx.service.AdminLoginService;
import com.lzx.service.BlogLoginService;
import com.lzx.utils.JwtUtil;
import com.lzx.utils.RedisCache;
import com.lzx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;
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
        redisCache.setCacheObject("adminlogin:"+userId, loginUser);

        //返回结果
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        String id = SecurityUtils.getUserId().toString();
        redisCache.deleteObject("adminlogin:" + id);
        return ResponseResult.okResult();
    }
}
