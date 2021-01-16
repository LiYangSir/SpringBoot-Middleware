package com.quguai.mapper;

import com.quguai.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ItemMapper {
    @Select("select * from item where code = #{code}")
    Item selectByCode(@Param("code") String code);
}
