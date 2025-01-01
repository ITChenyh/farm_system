package com.cyh.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.cyh.entity.Goods;
import com.cyh.entity.Orders;
import com.cyh.exception.CustomException;
import com.cyh.mapper.OrdersMapper;
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

    /**
     * 新增
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
        goods.setStore(store - orders.getNum());
        goodsService.updateById(goods);
        ordersMapper.insert(orders);
    }

    /**
     * 删除
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