package com.goodsple.features.myexchange.mapper;

import com.goodsple.features.myexchange.dto.MyExchangePostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyExchangePostMapper {

  List<MyExchangePostDto> selectMyExchangePosts(
      @Param("userId") Long userId,
      @Param("status") String status,
      @Param("offset") int offset,
      @Param("size") int size
  );

  int countMyExchangePosts(
      @Param("userId") Long userId,
      @Param("status") String status
  );

}
