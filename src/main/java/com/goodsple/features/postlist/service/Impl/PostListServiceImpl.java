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

    // 3차만 선택한 경우 2차 전체를 가져오기
    if ((secondIds == null || secondIds.isEmpty()) && thirdIds != null && !thirdIds.isEmpty()) {
      secondIds = postListMapper.findSecondIdsByThirdIds(thirdIds);
    }

    // 2차만 선택했거나, 3차만 선택했거나, 둘 다 선택한 경우 모두 처리
    return postListMapper.findPostsBySecondAndThird(secondIds, thirdIds);
  }

  @Override
  public List<Long> getSecondIdsByThirdIds(List<Long> thirdIds) {
    List<Long> secondIds = postListMapper.findSecondIdsByThirdIds(thirdIds);
    if (secondIds == null) secondIds = new ArrayList<>();
    return secondIds;
  }

}
