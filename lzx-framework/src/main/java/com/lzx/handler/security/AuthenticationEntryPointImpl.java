package com.lzx.handler.security;

import com.alibaba.fastjson.JSON;
import com.lzx.domain.ResponseResult;
import com.lzx.enums.AppHttpCodeEnum;
import com.lzx.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//认证错误异常处理类
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        ResponseResult result = null;
        if(authException instanceof BadCredentialsException){
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),
                    authException.getMessage());
        } else if (authException instanceof InsufficientAuthenticationException) {
            //当访问需要登陆的接口而没有登陆时，会throw权限不足的错误
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        } else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),
                    "认证或授权错误");
        }


        //相应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
