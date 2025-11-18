package com.goodsple.features.search.mapper;

import com.goodsple.features.search.dto.SearchPostResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchMapper {

    List<SearchPostResponse> searchPosts(@Param("keyword") String keyword);
}
