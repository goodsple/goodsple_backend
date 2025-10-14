package com.goodsple.features.category.mapper;

import com.goodsple.features.category.entity.SecondCate;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SecondCateMapper {

  @Select("SELECT * FROM second_cate WHERE second_cate_id = #{secondCateId}")
  SecondCate getSecondCateById(@Param("secondCateId") Long secondCateId);

  @Select("SELECT * FROM second_cate WHERE first_cate_id = #{firstCateId}")
  List<SecondCate> getAllSecondCateByFirstCateId(@Param("firstCateId") Long firstCateId);

  @Select("SELECT * FROM second_cate")
  List<SecondCate> getAllSecondCate();

  @Insert("INSERT INTO second_cate (cate_name, first_cate_id, sub_text) VALUES (#{cateName}, #{firstCateId}, #{subText})")
  @Options(useGeneratedKeys = true, keyProperty = "secondCateId")
  void insertCate(SecondCate secondCate);

  @Update("UPDATE second_cate SET cate_name = #{cateName}, sub_text = #{subText}, visibility = #{visibility} WHERE second_cate_id = #{secondCateId}")
  void updateCate(SecondCate secondCate);

  @Delete("DELETE FROM second_cate WHERE second_cate_id = #{secondCateId}")
  void deleteCateById(@Param("secondCateId") Long secondCateId);
}
