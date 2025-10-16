package com.goodsple.features.category.user.controller;

import com.goodsple.features.category.user.dto.UserSecondCategoryDto;
import com.goodsple.features.category.user.dto.UserThirdCategoryDto;
import com.goodsple.features.category.user.service.UserCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/categories")
@RequiredArgsConstructor
public class UserCategoryController {

  private final UserCategoryService userCategoryService;

  // ✅ 2차 카테고리 조회 (1차 기준)
  @GetMapping("/second/{firstCateId}")
  public List<UserSecondCategoryDto> getSecondCategories(@PathVariable Long firstCateId) {
    return userCategoryService.getSecondCategories(firstCateId);
  }

  // ✅ 3차 카테고리 조회 (2차 기준)
  @GetMapping("/third/{secondCateId}")
  public List<UserThirdCategoryDto> getThirdCategories(@PathVariable Long secondCateId) {
    return userCategoryService.getThirdCategories(secondCateId);
  }

  // ✅ 2차별 3차 카테고리 조회
  @PostMapping("/third/by-second")
  public Map<Long, List<UserThirdCategoryDto>> getThirdCategoriesBySecond(@RequestBody List<Long> secondIds) {
    return userCategoryService.getThirdCategoriesBySecond(secondIds);
  }

  @GetMapping("/third/all")
  public List<UserThirdCategoryDto> getAllThirdCategories() {
    return userCategoryService.getAllThirdCategories();
  }
}
