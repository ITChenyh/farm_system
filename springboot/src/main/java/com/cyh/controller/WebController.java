package com.cyh.controller;

import com.cyh.common.Result;
import com.cyh.entity.Account;
import com.cyh.entity.User;
import com.cyh.service.AdminService;
import com.cyh.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;


    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account ac = null;
        if ("ADMIN".equals(account.getRole())) {
            ac = adminService.login(account);
        }
        if ("USER".equals(account.getRole())) {
            ac = userService.login(account);
        }
        return Result.success(ac);
    }

    /**
     * 注册：只允许普通用户。管理员账号由管理员创建。
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        }
        if ("USER".equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

}
