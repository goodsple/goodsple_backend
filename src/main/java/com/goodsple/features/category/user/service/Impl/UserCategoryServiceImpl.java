package com.goodsple.features.category.user.service.Impl;

import com.goodsple.features.category.user.dto.UserSecondCategoryDto;
import com.goodsple.features.category.user.dto.UserThirdCategoryDto;
import com.goodsple.features.category.user.mapper.UserCategoryMapper;
import com.goodsple.features.category.user.service.UserCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserCategoryServiceImpl implements UserCategoryService {

  private final UserCategoryMapper userCategoryMapper;

  @Override
  public List<UserSecondCategoryDto> getSecondCategories(Long firstCateId) {
    return userCategoryMapper.findSecondCategories(firstCateId);
  }

  @Override
  public List<UserThirdCategoryDto> getThirdCategories(Long secondCateId) {
    return userCategoryMapper.findThirdCategories(secondCateId);
  }

  @Override
  public Map<Long, List<UserThirdCategoryDto>> getThirdCategoriesBySecond(List<Long> secondIds) {
    Map<Long, List<UserThirdCategoryDto>> result = new HashMap<>();
    for (Long secondId : secondIds) {
      List<UserThirdCategoryDto> thirdList = userCategoryMapper.findThirdCategories(secondId);
      result.put(secondId, thirdList);
    }
    return result;
  }

  @Override
  public List<UserThirdCategoryDto> getAllThirdCategories() {
    return userCategoryMapper.findAllThirdCategories();
  }
}
