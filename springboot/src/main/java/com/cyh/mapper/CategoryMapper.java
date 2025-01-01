package com.cyh.mapper;

import com.cyh.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作category相关数据接口
*/
public interface CategoryMapper {

    /**
      * 新增
    */
    int insert(Category category);

    /**
      * 删除
    */
    @Delete("delete from category where id = #{id}")
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Category category);

    /**
      * 根据ID查询
    */
    @Select("select * from category where id = #{id}")
    Category selectById(Integer id);

    /**
      * 查询所有
    */
    List<Category> selectAll(Category category);

    /**
     * 精确查询title
     */
    @Select("select * from category where title = #{title}")
    Category selectByTitle(String title);

}