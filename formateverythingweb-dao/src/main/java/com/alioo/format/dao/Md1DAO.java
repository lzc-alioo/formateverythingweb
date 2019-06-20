package com.alioo.format.dao;

import com.alioo.format.domain.Md1;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface Md1DAO {
    public static final String COL_ALL = " id, b32, b16, data ";
    public static final String TABLE = " md1 ";

    @Select(" select " + COL_ALL + " from " + TABLE + " where id = #{id} ")
    public Md1 findById(@Param("id") int id);

    @Select(" select " + COL_ALL + " from " + TABLE + " limit #{start}, #{count} ")
    public List<Md1> list(@Param("start") int start, @Param("count") int count);

    @Insert(" insert into " + TABLE + " set "
            + " b32 = #{b32}, "
            + " b16 = #{b16}, "
            + " data = #{data}")
    public int insert(Md1 bean);

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
    public int insertBatch(List<Md1> list);

    @Update(" update " + TABLE + " set "
            + " b32 = #{b32}, "
            + " b16 = #{b16}, "
            + " data = #{data}"
            + " where id = #{id} ")
    public int update(Md1 bean);
}