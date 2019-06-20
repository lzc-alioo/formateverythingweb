package com.alioo.format.dao;

import com.alioo.format.domain.Md;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MdDAO {
    public static final String COL_ALL = " id, b32, b16, data ";
    public static String TABLE = " md ";

    @Select(" select " + COL_ALL + " from " + TABLE + " where id = #{id} ")
    public Md findById(@Param("id") int id);

    @Select(" select " + COL_ALL + " from " + TABLE + " limit #{start}, #{count} ")
    public List<Md> list(@Param("start") int start, @Param("count") int count);

    @Insert(" insert into " + TABLE + " set "
            + " b32 = #{b32}, "
            + " b16 = #{b16}, "
            + " data = #{data}")
    public int insert(Md bean);

    @Insert({"<script>" + " insert into " + TABLE + " ( "
            + " b32, "
            + " b16, "
            + " data"
            + " ) values  "
            + "<foreach collection='list' item='item' index='index' separator=','>"
            + "("
            + "  #{item.b32}, "
            + "  #{item.b16}, "
            + "  #{item.data}"
            + ")"
            + "</foreach>"
            + "</script>"})
    public int insertBatch(List<Md> list);

    @Update(" update " + TABLE + " set "
            + " b32 = #{b32}, "
            + " b16 = #{b16}, "
            + " data = #{data}"
            + " where id = #{id} ")
    public int update(Md bean);
}