package com.lzx.filter;

import com.alibaba.fastjson.JSON;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.LoginUser;
import com.lzx.enums.AppHttpCodeEnum;
import com.lzx.utils.JwtUtil;
import com.lzx.utils.RedisCache;
import com.lzx.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //1.从header获取token
        String token = request.getHeader("token");
        //1.1如果没有token，放行，让后续filter处理
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        //2.得到userId，存入redis
        Claims claims;
        try {
            //2.1解析token
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            //2.2如果token已过期，返回重新登陆
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;
        }
        //2.3获取userId
        String userId = claims.getSubject();

        //3.将user存入context
        //3.1获取user
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:" + userId);
        //3.1.1如果redis过期等原因，返回重新登陆
        if(Objects.isNull(loginUser)){
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
            return;
        }
        //3.2存入context
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //4.放行
        filterChain.doFilter(request, response);
    }
}
