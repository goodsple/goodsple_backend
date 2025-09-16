package com.goodsple.features.myexchange.mapper;

import com.goodsple.features.myexchange.dto.MyExchangePostDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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

  // 거래상태 변경(업데이트)
  @Update("UPDATE exchange_post " +
      "SET post_trade_status = #{status}, exchange_post_updated_at = NOW() " +
      "WHERE exchange_post_id = #{postId} AND user_id = #{userId}")
  int updatePostStatus(
      @Param("postId") Long postId,
      @Param("userId") Long userId,
      @Param("status") String status
  );

}
