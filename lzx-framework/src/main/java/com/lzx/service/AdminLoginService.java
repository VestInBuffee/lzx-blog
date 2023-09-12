package com.lzx.service;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.User;

public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
