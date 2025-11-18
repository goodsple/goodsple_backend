package com.goodsple.features.popular.mapper;

import com.goodsple.features.popular.dto.PopularKeywordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PopularKeywordMapper {

  List<PopularKeywordDTO> selectTopKeywords(@Param("limit") int limit);

  int incrementSearchCount(@Param("keyword") String keyword);

  int insertKeyword(@Param("keyword") String keyword);

}
