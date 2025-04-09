package com.cyh.redis;

import com.cyh.entity.Goods;
import com.cyh.mapper.SeckillMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * 程序启动时Redis加载秒杀商品
 */
@Component
public class StockWarmUp {

    @Autowired
    private RedisTemplate<String, Object> myRedisTemplate;
    @Autowired
    private SeckillMapper seckillMapper; // 假设存在商品Mapper

    @PostConstruct
    public void initStock() {
        List<Goods> products = seckillMapper.selectAll();
        products.forEach(product -> {
            String redisKey = "goods:" + product.getId() + "_stock";
            myRedisTemplate.opsForValue().set(redisKey, product.getStore());
        });
    }
}
