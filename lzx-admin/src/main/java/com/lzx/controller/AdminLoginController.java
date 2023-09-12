package com.lzx.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.User;
import com.lzx.domain.vo.AdminUserInfoVo;
import com.lzx.domain.vo.UserInfoVo;
import com.lzx.service.AdminLoginService;
import com.lzx.service.BlogLoginService;

import com.lzx.service.MenuService;
import com.lzx.service.RoleService;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lzx
 */
@RestController
//@RequestMapping("/user")
public class AdminLoginController {
    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return adminLoginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> userInfo(){
        User originUser = SecurityUtils.getLoginUser().getUser();
        //获取permissions
        List<String> permissions = menuService.getPermissionsByUserId(originUser.getId());
        //获取role_key
        List<String> roles = roleService.getRoleKeysByUserId(originUser.getId());
        //把user序列化为UserInfoVo
        UserInfoVo user = BeanCopyUtils.copyBean(originUser, UserInfoVo.class);
        //封装成AdminUserInfoVo
        AdminUserInfoVo adminUserInfoVo =
                new AdminUserInfoVo(permissions, roles, user);
        //返回
        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        return menuService.getRouters();
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }
}
