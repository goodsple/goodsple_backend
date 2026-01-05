package com.goodsple.features.postlist.service.Impl;

import com.goodsple.features.postlist.dto.PostFilterDto;
import com.goodsple.features.postlist.dto.PostListDto;
import com.goodsple.features.postlist.mapper.PostListMapper;
import com.goodsple.features.postlist.service.PostListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostListServiceImpl implements PostListService {

  private final PostListMapper postListMapper;

  @Override
  public List<PostListDto> getAllPosts() {
    return postListMapper.findAllPosts();
  }

  @Override
  public List<PostListDto> getPostsByCategory(Long categoryId) {
    return postListMapper.findPostsByCategory(categoryId);
  }


  @Override
  public List<PostListDto> getPostsBySecondAndThird(PostFilterDto filterDto) {

    List<Long> secondIds = filterDto.getSecondIds();
    List<Long> thirdIds = filterDto.getThirdIds();

    // 1️⃣ 3차가 선택된 경우
    if (thirdIds != null && !thirdIds.isEmpty()) {

      // 1-1. 같은 이름의 모든 3차 확장 (EXO 전체)
      List<Long> expandedThirdIds =
          postListMapper.findThirdIdsBySameName(thirdIds);

      // 1-2. 🔥 2차도 같이 선택된 경우 → 교집합
      if (secondIds != null && !secondIds.isEmpty()) {
        List<Long> thirdIdsBySecond =
            postListMapper.findThirdIdsBySecondIds(secondIds);

        expandedThirdIds.retainAll(thirdIdsBySecond);
      }

      // 교집합 결과가 없으면 빈 리스트
      if (expandedThirdIds.isEmpty()) {
        return List.of();
      }

      filterDto.setThirdIds(expandedThirdIds);
      return postListMapper.findPostsBySecondAndThird(filterDto);
    }

    // 2️⃣ 3차 없고 2차만 선택
    if (secondIds != null && !secondIds.isEmpty()) {

      List<Long> thirdIdsBySecond =
          postListMapper.findThirdIdsBySecondIds(secondIds);

      if (thirdIdsBySecond.isEmpty()) {
        return List.of();
      }

      filterDto.setThirdIds(thirdIdsBySecond);
      return postListMapper.findPostsBySecondAndThird(filterDto);
    }

    // 3️⃣ 아무 필터도 없으면 전체
    return postListMapper.findAllPosts();
  }


}
