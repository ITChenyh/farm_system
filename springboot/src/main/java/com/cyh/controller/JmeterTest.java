package com.cyh.controller;

import com.cyh.common.Result;
import com.cyh.entity.Admin;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jmeter")
public class JmeterTest {
    /**
     * 新增
     */
    @GetMapping("/test")
    public Result test() {
        return Result.success();
    }
}
