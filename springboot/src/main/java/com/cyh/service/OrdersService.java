package com.cyh.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.cyh.entity.Goods;
import com.cyh.entity.Orders;
import com.cyh.exception.CustomException;
import com.cyh.mapper.OrdersMapper;
import com.cyh.mapper.SeckillMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单信息业务处理
 **/
@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private GoodsService goodsService;
    @Resource
    private SeckillMapper seckillMapper;

    /**
     * 新增订单
     */
    @Transactional
    public void add(Orders orders) {
        orders.setOrderNo(IdUtil.fastSimpleUUID());
        orders.setTime(DateUtil.now());

        //查询现在数据库的库存
        Goods goods = goodsService.selectById(orders.getGoodsId());
        if(goods == null){
            throw new CustomException("商品不存在");
        }
        int store = goods.getStore();
        if(store < orders.getNum()){
            throw new CustomException("商品库存不足");
        }

        Integer ifSuc = goodsService.orderUpdateById(goods.getId(), orders.getNum());
        if(ifSuc < 1){
            System.out.println("抢购失败啦");
            throw new CustomException("抢购失败");
        }
        ordersMapper.insert(orders); //可以改成向mq发送消息
    }

    /**
     * 直接插入订单
     */
    @Transactional
    public void insert(Orders orders) {
        ordersMapper.insert(orders);
    }

    /**
     * 普通商品订单删除
     */
    public void deleteById(Integer id) {
        Orders o = ordersMapper.selectById(id);
        Goods g = goodsService.selectById(o.getGoodsId());
        if(g != null){
            g.setStore(o.getNum() + g.getStore());
            goodsService.updateById(g);
        }
        ordersMapper.deleteById(id);
    }

    /**
     * 秒杀商品订单删除，MySQL数据库处理
     */
    public void deleteSeckillOrderById(Integer id, Integer goodsId) {
        ordersMapper.deleteById(id);
        seckillMapper.increStoreById(goodsId);
    }

    /**
     * 修改
     */
    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    /**
     * 根据ID查询
     */
    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    /**
     * 根据订单ID查询
     */
    public Orders selectByOrderId(String orderId) {
        return ordersMapper.selectByOrderId(orderId);
    }

    /**
     * 根据Name查询
     */
    public Orders selectByName(String name) {
        return ordersMapper.selectByName(name);
    }

    /**
     * 查询所有
     */
    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    /**
     * 分页查询
     */
    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

}