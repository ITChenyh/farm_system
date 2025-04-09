package com.cyh.mapper;

import com.cyh.entity.Goods;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.ResultSetType;

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
     * 防超卖，订单下单后，商品库存修改
     */
    int orderUpdateById(Integer id, int number);

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
     * 自定义分页查询
     * @param lastPageId 上一页最后一条数据的ID
     */
    List<Goods> selectByPage(Integer lastPageId, Integer pageSize);

    /**
     * 精确查询name
     */
    @Select("select * from goods where name = #{name}")
    Goods selectByName(String name);

    @Select("SELECT * FROM goods WHERE id BETWEEN #{startId} AND #{endId} ORDER BY id")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000) // 流式读取配置
    List<Goods> selectByIdRange(int startId, int endId);


}