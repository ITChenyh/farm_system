package com.cyh.common;

import cn.hutool.core.date.DateTime;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;

// 示例：权限校验拦截器
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("到拦截器了，jmeter测试请求" + request.getRequestURI() + " 端口号: " + request.getRemotePort() + " " + new DateTime());
        String token = request.getHeader("Authorization");
        if (!validateToken(token)) {
            response.setStatus(401);
            return false; // 拦截请求
        }

        return true; // 放行请求
    }

    private boolean validateToken(String token) {
        // 实现具体校验逻辑
        return token != null && MyJWTUtil.verifyToken(token);
    }
}

