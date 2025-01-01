package com.cyh.service;

import com.cyh.entity.Goods;
import com.cyh.entity.GoodsStock;
import com.cyh.mapper.GoodsStockMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 农产品进货业务处理
 **/
@Service
public class GoodsStockService {

    @Resource
    private GoodsStockMapper goodsStockMapper;

    @Autowired
    private GoodsService goodsService;

    /**
     * 新增
     */
    @Transactional
    public void add(GoodsStock goodsStock) {
        //更新农产品数量
        Goods g = goodsService.selectByName(goodsStock.getGoodsName());
        g.setStore(g.getStore() + goodsStock.getNum());
        goodsService.updateById(g);
        goodsStockMapper.insert(goodsStock);
    }

    /**
     * 删除
     */
    @Transactional
    public void deleteById(Integer id) {
        goodsStockMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(GoodsStock goodsStock) {
        goodsStockMapper.updateById(goodsStock);
    }

    /**
     * 根据ID查询
     */
    public GoodsStock selectById(Integer id) {
        return goodsStockMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<GoodsStock> selectAll(GoodsStock goodsStock) {
        return goodsStockMapper.selectAll(goodsStock);
    }

    /**
     * 分页查询
     */
    public PageInfo<GoodsStock> selectPage(GoodsStock goodsStock, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        System.out.println("goodsStock = " + goodsStock.getGoodsName());
        List<GoodsStock> list = goodsStockMapper.selectAll(goodsStock);
        return PageInfo.of(list);
    }

}