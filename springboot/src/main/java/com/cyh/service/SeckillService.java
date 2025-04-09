package com.cyh.service;

import com.cyh.config.RedisConfig;
import com.cyh.entity.Goods;
import com.cyh.entity.Orders;
import com.cyh.exception.CustomException;
import com.cyh.mapper.SeckillMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SeckillMapper seckillMapper;

    @Resource
    private RedisStockService redisStockService;

    // 双写，数据同步
    public void saveGoods(Goods goods) {
        // 写入MySQL
        seckillMapper.insert(goods);
        int id = goods.getId();
        // 同步写入Redis
        String redisKey = "goods:" + id + "_stock";
        System.out.println(redisKey);
        redisTemplate.opsForValue().set(redisKey, goods.getStore());
    }

    /**
     * 分页查询
     */
    public PageInfo<Goods> selectPage(Integer pageNum, Integer pageSize) {
        System.out.println("到service了");
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> list = seckillMapper.selectAll();
        return PageInfo.of(list);
    }

    /**
     * 秒杀产品下单
     */
    public void buy(Orders orders) {
        if(orders.getNum() > 1){
            throw new CustomException("抢购数量超出限制");
        }
        redisStockService.deductStock(orders.getGoodsId(), orders.getUserId());
    }




}
