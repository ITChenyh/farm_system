package com.cyh.mapper;

import com.cyh.entity.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作orders相关数据接口
*/
public interface OrdersMapper {

    /**
      * 新增
    */
    int insert(Orders orders);

    /**
      * 删除
    */
    @Delete("delete from orders where id = #{id}")
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Orders orders);

    /**
      * 根据数据库表ID查询
    */
    @Select("select * from orders where id = #{id}")
    Orders selectById(Integer id);

    /**
     * 根据订单ID查询
     */
    @Select("select * from orders where order_no = #{orderId}")
    Orders selectByOrderId(String orderId);

    /**
      * 查询所有，分管理员和普通用户
    */
    List<Orders> selectAll(Orders orders);

    /**
     * 精确查询name
     */
    @Select("select * from orders where name = #{name}")
    Orders selectByName(String name);

}