package com.cyh.service;

import com.cyh.elesticsearch.EsGoods;
import com.cyh.elesticsearch.GoodsESRepository;
import com.cyh.entity.Goods;
import com.cyh.mapper.GoodsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 农产品信息业务处理
 **/
@Service
@RequiredArgsConstructor
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private GoodsESRepository goodsESRepository;


    // 双写，数据同步
    public void saveGoods(Goods goods) {
        // 写入MySQL
        goodsMapper.insert(goods);
        // 同步写入ES
        goodsESRepository.save(goods);
    }

    // ES搜索服务
    public Page<Goods> search(String keyword, int page, int size) {
        Page<Goods> p =  goodsESRepository.search(
                keyword,
                PageRequest.of(page, size, Sort.by("_score").descending())
        );
        return goodsESRepository.search(
                keyword,
                PageRequest.of(page, size, Sort.by("_score").descending())
        );
    }

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
        goodsESRepository.deleteById((long) id);

    }

    /**
     * 修改
     */
    public void updateById(Goods goods) {
        goodsMapper.updateById(goods);
    }

    /**
     * 防超卖，订单下单后，商品库存修改
     */
    public Integer orderUpdateById(Integer id, int number) {

        return goodsMapper.orderUpdateById(id, number);
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
        //TODO 要改前端，传上一页的lastID
        //List<Goods> list = goodsMapper.selectByPage();
        List<Goods> list = goodsMapper.selectAll(goods);
        return PageInfo.of(list);
    }

}