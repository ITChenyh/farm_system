package com.cyh.mapper;

import com.cyh.entity.Goods;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SeckillMapper {

    int insert(Goods goods);

    List<Goods> selectAll();

    @Update("update seckill_goods set store = store + 1 where id = #{id}")
    int increStoreById(Integer id);

    @Update("update seckill_goods set store = store - 1 where id = #{id}")
    int decreStoreById(Integer id);


}
