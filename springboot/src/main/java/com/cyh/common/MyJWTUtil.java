package com.cyh.common;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.cyh.entity.Account;

import java.util.HashMap;
import java.util.Map;

public class MyJWTUtil {

    //私钥
    private static final String TOKEN_SECRET = "farmSecret";

    public static String generateToken(Account account) {
        DateTime now = DateTime.now();
        Map<String, Object> payload = new HashMap<>();
        payload.put(JWTPayload.ISSUER, "cyh"); // 签发人
        payload.put(JWTPayload.EXPIRES_AT, now.offset(DateField.HOUR, 2)); // 过期时间
        // 自定义业务字段
        payload.put("id", account.getId());
        payload.put("username", account.getUsername());
        payload.put("name", account.getName());
        payload.put("password", account.getPassword());
        payload.put("role", account.getRole());
        payload.put("newPassword", account.getNewPassword());
        payload.put("avatar", account.getAvatar());

        String token = cn.hutool.jwt.JWTUtil.createToken(payload, JWTSignerUtil.hs256(MyJWTUtil.TOKEN_SECRET.getBytes()));

        return token;
    }

    public static boolean verifyToken(String token){
        if (JWTUtil.verify(token, MyJWTUtil.TOKEN_SECRET.getBytes())) {
            return true;
        }
        else{
            return false;
        }
    }

    public static Account parseToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            // 将用户角色绑定到请求上下文
            Integer id = (Integer) jwt.getPayload("id");
            String username = (String) jwt.getPayload("username");
            String name = (String) jwt.getPayload("name");
            String password = (String) jwt.getPayload("password");
            String role = (String) jwt.getPayload("role");
            String newPassword = (String) jwt.getPayload("newPassword");
            String avatar = (String) jwt.getPayload("avatar");

            Account account = new Account(id, username, name, password, role, newPassword, avatar);

            return account;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
