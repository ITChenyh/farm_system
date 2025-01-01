package com.cyh.service;

import com.cyh.entity.Goods;
import com.cyh.mapper.GoodsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 农产品信息业务处理
 **/
@Service
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    /**
     * 新增
     */
    public void add(Goods goods) {
        goodsMapper.insert(goods);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        goodsMapper.deleteById(id);
    }

    /**
     * 修改
     */
    public void updateById(Goods goods) {
        goodsMapper.updateById(goods);
    }

    /**
     * 根据ID查询
     */
    public Goods selectById(Integer id) {
        return goodsMapper.selectById(id);
    }

    /**
     * 根据Name查询
     */
    public Goods selectByName(String name) {
        return goodsMapper.selectByName(name);
    }

    /**
     * 查询所有
     */
    public List<Goods> selectAll(Goods goods) {
        return goodsMapper.selectAll(goods);
    }

    /**
     * 分页查询
     */
    public PageInfo<Goods> selectPage(Goods goods, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        System.out.println("goods = " + goods.getCategoryName());
        List<Goods> list = goodsMapper.selectAll(goods);
        return PageInfo.of(list);
    }

}