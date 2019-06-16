package com.alioo.format.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.alioo.format.domain.Md0;

@Mapper
public interface Md0DAO {
    public static final String COL_ALL = " id, b32, b16, data ";
    public static final String TABLE = " md0 ";

    @Select(" select " + COL_ALL + " from " + TABLE + " where id = #{id} ")
    public Md0 findById(@Param("id") int id);

    @Select(" select " + COL_ALL + " from " + TABLE + " limit #{start}, #{count} ")
    public List<Md0> list(@Param("start") int start, @Param("count") int count);

    @Insert(" insert into " + TABLE + " set "
            + " b32 = #{b32}, "
            + " b16 = #{b16}, "
            + " data = #{data}")
    public int insert(Md0 bean);

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
    public int insertBatch(List<Md0> list);

    @Update(" update " + TABLE + " set "
            + " b32 = #{b32}, "
            + " b16 = #{b16}, "
            + " data = #{data}"
            + " where id = #{id} ")
    public int update(Md0 bean);
}