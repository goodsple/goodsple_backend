package com.goodsple.features.category.user.mapper;

import com.goodsple.features.category.user.dto.UserSecondCategoryDto;
import com.goodsple.features.category.user.dto.UserThirdCategoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserCategoryMapper {

  List<UserSecondCategoryDto> findSecondCategories(@Param("firstCateId") Long firstCateId);

  List<UserThirdCategoryDto> findThirdCategories(@Param("secondCateId") Long secondCateId);

  List<UserThirdCategoryDto> findAllThirdCategories();

}

