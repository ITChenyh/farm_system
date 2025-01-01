package com.cyh.mapper;

import com.cyh.entity.GoodsStock;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作goodsStock相关数据接口
*/
public interface GoodsStockMapper {

    /**
      * 新增
    */
    int insert(GoodsStock goodsStock);

    /**
      * 删除
    */
    @Delete("delete from goods_stock where id = #{id}")
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(GoodsStock goodsStock);

    /**
      * 根据ID查询
    */
    @Select("select * from goods_stock where id = #{id}")
    GoodsStock selectById(Integer id);

    /**
      * 查询所有
    */
    List<GoodsStock> selectAll(GoodsStock goodsStock);

}