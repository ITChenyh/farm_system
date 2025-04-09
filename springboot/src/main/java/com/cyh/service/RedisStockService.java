package com.cyh.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.cyh.config.RabbitMQConfig;
import com.cyh.entity.Orders;
import com.cyh.entity.RollbackMessage;
import com.cyh.exception.CustomException;
import com.cyh.mapper.SeckillMapper;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class RedisStockService {

    @Resource
    private RedisTemplate<String, Object> myRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private OrdersService orderService; // 订单服务
    @Autowired
    private AmqpTemplate rabbitTemplate;

    private static final String STOCK_PREFIX = "goods:";
    private static final String STOCK_STREAM = "stock_rollback_stream";
    @Autowired
    private SeckillMapper seckillMapper;

    /**
     * 预扣库存（原子操作）
     * @param goodsId 商品ID
     * @param userId 用户ID
     * @return 预扣结果
     */
    public void deductStock(Integer goodsId, Integer userId) {
        String stockKey = STOCK_PREFIX + goodsId + "_stock";
        String lockKey = "lock:" + stockKey;


        // 分布式锁防止并发问题
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(1, 10, TimeUnit.SECONDS)) {
                // 使用Lua脚本保证原子性
                String script =
                    "local stock = tonumber(redis.call('get', tostring(KEYS[1]))) or 0; " +
                    "if stock <= 0 then return 0 else " +
                    "redis.call('decrby', tostring(KEYS[1]), 1); " +
                    "return 1 end";

                DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
                Long result = (Long)myRedisTemplate.execute(redisScript, Collections.singletonList(stockKey));
                // 修改后代码（增加补偿逻辑）
                if (result == 1) {
                    String orderId = IdUtil.getSnowflakeNextIdStr(); // 雪花算法生成订单ID
                    try {
                        System.out.println("创建订单 雪花ID：" + orderId);
                        createPreOrder(orderId, goodsId, userId); // 创建预订单

                    } catch (Exception e) {
                        // 数据库失败时回滚Redis库存
                        myRedisTemplate.opsForValue().increment(stockKey, 1);
                        throw new CustomException("抢购失败");
                    }
                    sendRollbackMessage(orderId, goodsId);    // 发送延时消息
                }
                else{
                    throw new CustomException("抢购失败");
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CustomException("抢购失败");
        } finally {
            lock.unlock();
        }
    }

    // 发送库存回滚延时消息（使用Redis Stream）
    private void sendRollbackMessage(String orderId, Integer goodsId) {
        RollbackMessage message = new RollbackMessage(orderId, goodsId, System.currentTimeMillis());
        System.out.println("发送库存回滚延时消息ID：" + orderId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DELAY_EXCHANGE,
                RabbitMQConfig.DELAY_ROUTING_KEY,
                message,
                msg -> {
                    msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    System.out.println("msg：" + msg);
                    return msg;
                }
        );
    }

    // 创建预订单（数据库事务）
    @Transactional
    public void createPreOrder(String orderNo, Integer goodsId, Integer userId) {
        Orders order = new Orders();
        order.setOrderNo(orderNo);
        order.setStatus("待支付");
        order.setTime(DateUtil.now());
        order.setUserId(userId);
        order.setGoodsId(goodsId);
        order.setNum(1);
        orderService.insert(order);
        seckillMapper.decreStoreById(order.getGoodsId());

    }
}


