package com.lzx.controller;

import com.lzx.annotation.SystemLog;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.User;
import com.lzx.enums.AppHttpCodeEnum;
import com.lzx.service.UserService;
import com.lzx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }
    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult putUserInfo(@RequestBody User user){
        //如果用户尝试更新其他user
        if(!user.getId().equals(SecurityUtils.getUserId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.UPDATE_OTHER_USERINFO);
        }

        return userService.putUserInfo(user);
    }
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
