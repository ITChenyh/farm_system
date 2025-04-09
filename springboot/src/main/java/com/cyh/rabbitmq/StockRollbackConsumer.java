package com.cyh.rabbitmq;

import com.cyh.config.RabbitMQConfig;
import com.cyh.entity.Orders;
import com.cyh.entity.RollbackMessage;
import com.cyh.service.OrdersService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StockRollbackConsumer {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private OrdersService ordersService;

    @RabbitListener(queues = RabbitMQConfig.DEAD_QUEUE)
    @Transactional
    public void processRollbackMessage(@Payload RollbackMessage message) {
        try {
            // 检查订单状态
            Orders orders = ordersService.selectByOrderId(message.getOrderId());
            System.out.println("死信队列消息 订单ID：" + message.getOrderId());
            System.out.println("order信息：" + orders.getStatus());
            if (orders != null && orders.getStatus().equals("待支付")) {
                System.out.println("执行回滚...");
                // 执行库存回滚
                String stockKey = "goods:" + message.getGoodsId() + "_stock";
                redisTemplate.opsForValue().increment(stockKey, 1);
                
                // 标记订单失效
                ordersService.deleteSeckillOrderById(orders.getId(), orders.getGoodsId());
            }
        } catch (Exception e) {
            // 记录异常并加入重试队列
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
