package com.goodsple.features.exchange.service.Impl;

import com.goodsple.features.exchange.dto.PostCategoryDto;
import com.goodsple.features.exchange.mapper.PostCategoryMapper;
import com.goodsple.features.exchange.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCategoryServiceImpl implements PostCategoryService {

  private final PostCategoryMapper postCategoryMapper;

  @Override
  public List<PostCategoryDto> getAllCategories() {
    return postCategoryMapper.findAllThirdCategories();
  }
}
