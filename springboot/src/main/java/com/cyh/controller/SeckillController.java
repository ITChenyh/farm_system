package com.cyh.controller;

import com.cyh.common.Result;
import com.cyh.entity.Goods;
import com.cyh.entity.Orders;
import com.cyh.service.GoodsStockService;
import com.cyh.service.RedisStockService;
import com.cyh.service.SeckillService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 秒杀商品操作接口
 **/
@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Resource
    GoodsStockService goodsStockService;
    @Resource
    SeckillService seckillService;

    /**
     * 新增商品
     */
    @PostMapping("/add")
    public Result add(@RequestBody Goods goods) {
        seckillService.saveGoods(goods);
        return Result.success();
    }

    /**
     * 下单商品
     */
    @PostMapping("/buy")
    public Result buy(@RequestBody Orders orders) {
        seckillService.buy(orders);
        return Result.success();
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Goods> page = seckillService.selectPage(pageNum, pageSize);
        System.out.println("page = " + page);
        return Result.success(page);
    }
}
