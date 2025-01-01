package com.cyh.mapper;

import com.cyh.entity.Notice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作notice相关数据接口
*/
public interface NoticeMapper {

    /**
      * 新增
    */
    int insert(Notice notice);

    /**
      * 删除
    */
    @Delete("delete from notice where id = #{id}")
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Notice notice);

    /**
      * 根据ID查询
    */
    @Select("select * from notice where id = #{id}")
    Notice selectById(Integer id);

    /**
      * 查询所有
    */
    List<Notice> selectAll(Notice notice);

    /**
     * 精确查询title
     */
    @Select("select * from notice where title = #{title}")
    Notice selectByTitle(String title);

}