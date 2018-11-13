package com.alioo.format.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.alioo.format.domain.Article;

@Mapper
public interface ArticleDAO {
    public static final String COL_ALL = " id, title, content, readCount, commentCount, csdnLink, graspTime, contentType, contentDesc, create_time, create_pin, update_time, update_pin, sys_version, yn, ts "; 
    public static final String TABLE = " article "; 

    @Select(" select " + COL_ALL + " from " + TABLE + " where id = #{id} ") 
    public Article findById(@Param("id") Long id);

    @Select(" select " + COL_ALL + " from " + TABLE + " limit #{start}, #{count} ") 
    public List<Article> list(@Param("start") int start, @Param("count") int count);

    @Select("<script>" +
            " select " + COL_ALL +
            " from " + TABLE +
            " where 1 = 1 " +
            " <if test=\"query.title != null \"> and title = %#{query.title}% </if>" +
            " <if test=\"sortOrder != null \"> order by #{sortOrder}</if>" +
            " limit #{start}, #{pageSize} " +
            "</script>")
    public List<Article> listQueryPage( @Param("query")Article query, @Param("start")int start, @Param("pageSize")int
            pageSize, @Param("sortOrder")String sortOrder);

    @Select("<script>" +
            " select count(1)"  +
            " from " + TABLE +
            " where 1=1" +
            " <if test=\"title != null \"> and title = %#{title}% </if>" +
            "</script>")
    public Integer listQueryCount(Article query);


    @Insert(" insert into " + TABLE + " set "
        + " title = #{title}, "
        + " content = #{content}, "
        + " readCount = #{readCount}, "
        + " commentCount = #{commentCount}, "
        + " csdnLink = #{csdnLink}, "
        + " graspTime = #{graspTime}, "
        + " contentType = #{contentType}, "
        + " contentDesc = #{contentDesc}, "
        + " create_time = now(), "
        + " create_pin = #{createPin}, "
        + " update_time = #{updateTime}, "
        + " update_pin = #{updatePin}, "
        + " sys_version = #{sysVersion}, "
        + " yn = #{yn}, "
        + " ts = #{ts}")
    public int insert(Article bean);

    @Update(" update " + TABLE + " set "
        + " title = #{title}, "
        + " content = #{content}, "
        + " readCount = #{readCount}, "
        + " commentCount = #{commentCount}, "
        + " csdnLink = #{csdnLink}, "
        + " graspTime = #{graspTime}, "
        + " contentType = #{contentType}, "
        + " contentDesc = #{contentDesc}, "
        + " create_time = now(), "
        + " create_pin = #{createPin}, "
        + " update_time = #{updateTime}, "
        + " update_pin = #{updatePin}, "
        + " sys_version = #{sysVersion}, "
        + " yn = #{yn}, "
        + " ts = #{ts}"
        + " where id = #{id} ")
    public int update(Article bean);
}