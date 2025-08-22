package com.goodsple.features.exchange.mapper;

import com.goodsple.features.exchange.dto.PostCategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostCategoryMapper {

  List<PostCategoryDto> findAllThirdCategories();

}
