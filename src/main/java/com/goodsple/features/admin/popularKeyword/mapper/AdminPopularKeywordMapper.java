package com.goodsple.features.admin.popularKeyword.mapper;

import com.goodsple.features.admin.popularKeyword.dto.PopularKeywordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminPopularKeywordMapper {

  List<PopularKeywordDTO> selectAllKeywords();

  int updateKeywordStatus(@Param("keywordId") Long keywordId,
                          @Param("status") String status);

}
