package com.cyh.mapper;

import com.cyh.entity.Goods;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作goods相关数据接口
*/
public interface GoodsMapper {

    /**
      * 新增
    */
    int insert(Goods goods);

    /**
      * 删除
    */
    @Delete("delete from goods where id = #{id}")
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Goods goods);

    /**
      * 根据ID查询
    */
    @Select("select * from goods where id = #{id}")
    Goods selectById(Integer id);

    /**
      * 查询所有
    */
    List<Goods> selectAll(Goods goods);

    /**
     * 精确查询name
     */
    @Select("select * from goods where name = #{name}")
    Goods selectByName(String name);

}