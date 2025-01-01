package com.cyh.mapper;

import com.cyh.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作user相关数据接口
*/
public interface UserMapper {

    /**
      * 新增
    */
    int insert(User user);

    /**
      * 删除
    */
    @Delete("delete from user where id = #{id}")
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(User user);

    /**
      * 根据ID查询
    */
    @Select("select * from user where id = #{id}")
    User selectById(Integer id);

    /**
      * 查询所有
    */
    List<User> selectAll(User user);

    @Select("select * from user where username = #{username}")
    User selectByUsername(String username);

}
