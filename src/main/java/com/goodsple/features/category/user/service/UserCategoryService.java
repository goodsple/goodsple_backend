package com.goodsple.features.category.user.service;

import com.goodsple.features.category.user.dto.UserSecondCategoryDto;
import com.goodsple.features.category.user.dto.UserThirdCategoryDto;

import java.util.List;
import java.util.Map;

public interface UserCategoryService {
  List<UserSecondCategoryDto> getSecondCategories(Long firstCateId);
  List<UserThirdCategoryDto> getThirdCategories(Long secondCateId);


  Map<Long, List<UserThirdCategoryDto>> getThirdCategoriesBySecond(List<Long> secondIds);

  List<UserThirdCategoryDto> getAllThirdCategories();

}
