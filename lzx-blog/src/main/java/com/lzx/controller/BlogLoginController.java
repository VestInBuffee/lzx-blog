package com.lzx.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.User;
import com.lzx.enums.AppHttpCodeEnum;
import com.lzx.handler.exception.SystemException;
import com.lzx.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author lzx
 */
@RestController
public class BlogLoginController {
    @Autowired
    BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
