package com.goodsple.features.category.mapper;

import com.goodsple.features.category.entity.FirstCate;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FirstCateMapper {

    @Select("SELECT * FROM first_cate")
    List<FirstCate> getAllFirstCate();

    @Select("SELECT * FROM first_cate WHERE first_cate_id = #{firstCateId}")
    FirstCate getFirstCateById(@Param("firstCateId") Long firstCateId);

    @Insert("INSERT INTO first_cate (cate_name) VALUES (#{cateName})")
    @Options(useGeneratedKeys = true, keyProperty = "firstCateId")
    void insertCate(FirstCate firstCate);

    @Update("UPDATE first_cate SET cate_name = #{cateName} WHERE first_cate_id = #{firstCateId}")
    void updateCate(FirstCate firstCate);

    @Delete("DELETE FROM first_cate WHERE first_cate_id = #{firstCateId}")
    void deleteCateById(@Param("firstCateId") Long id);
}
