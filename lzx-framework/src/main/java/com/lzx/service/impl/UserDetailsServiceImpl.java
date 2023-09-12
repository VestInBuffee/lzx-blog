package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.entity.LoginUser;
import com.lzx.domain.entity.User;
import com.lzx.mapper.UserMapper;
import com.lzx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private MenuService menuService;
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //首先查询指定user
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        //判断是否有用户
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户
        // 如果是管理员
        if(SystemConstants.USER_TYPE_ADMIN.equals(user.getType())){
            List<String> perms = menuService.getPermissionsByUserId(user.getId());
            return new LoginUser(user, perms);
        }
        return new LoginUser(user, null);
    }
}
