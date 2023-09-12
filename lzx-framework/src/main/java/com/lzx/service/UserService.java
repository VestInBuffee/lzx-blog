package com.lzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddUserDto;
import com.lzx.domain.dto.ListUserDto;
import com.lzx.domain.dto.UpdateUserDto;
import com.lzx.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-08-13 17:12:24
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult putUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUserList(Integer pageNum, Integer pageSize, ListUserDto listUserDto);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteUserById(Long id);

    ResponseResult getUserById(Long id);

    ResponseResult updateUser(UpdateUserDto updateUserDto);
}

