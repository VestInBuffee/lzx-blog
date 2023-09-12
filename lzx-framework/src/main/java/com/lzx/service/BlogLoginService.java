package com.lzx.service;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.User;

public interface BlogLoginService {

    ResponseResult login(User user);

    ResponseResult logout();
}
