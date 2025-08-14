package com.goodsple.features.category.mapper;

import com.goodsple.features.category.entity.ThirdCate;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ThirdCateMapper {

    @Select("SELECT * FROM third_cate WHERE third_cate_id = #{thirdCateId}")
    ThirdCate getThirdCateById(@Param("thirdCateId") Long thirdCateId);

    @Select("SELECT * FROM third_cate WHERE second_cate_id = #{secondCateId}")
    List<ThirdCate> getAllThirdCateBySecondCateId(@Param("secondCateId") Long secondCateId);

    @Select("SELECT * FROM third_cate")
    List<ThirdCate> getAllThirdCate();

    @Insert("INSERT INTO third_cate (cate_name, second_cate_id, sub_text) VALUES (#{cateName}, #{secondCateId}, #{subText})")
    @Options(useGeneratedKeys = true, keyProperty = "thirdCateId")
    void insertCate(ThirdCate thirdCate);

    @Update("UPDATE third_cate SET cate_name = #{cateName} WHERE third_cate_id = #{thirdCateId}")
    void updateCate(ThirdCate thirdCate);

    @Delete("DELETE FROM third_cate WHERE third_cate_id = #{thirdCateId}")
    void deleteCateById(@Param("thirdCateId") Long id);

}
