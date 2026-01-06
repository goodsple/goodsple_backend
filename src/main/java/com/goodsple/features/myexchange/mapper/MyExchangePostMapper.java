package com.goodsple.features.myexchange.mapper;

import com.goodsple.features.myexchange.dto.ChatUserResponseDto;
import com.goodsple.features.myexchange.dto.MyExchangePostDto;
import com.goodsple.features.myexchange.dto.MyExchangePostUpdateDto;
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
  int updatePostStatus(@Param("postId") Long postId,
                       @Param("userId") Long userId,
                       @Param("status") String status);

  // 판매자만 거래상대 목록 확인가능
  boolean isPostOwner(@Param("postId") Long postId,
                      @Param("userId") Long userId);


  List<ChatUserResponseDto> selectChatUsersByPostId (@Param("postId") Long postId,
                                                    @Param("currentUserId") Long currentUserId);



  // 거래상대 확정
  int updateBuyer (@Param("postId") Long postId,
                   @Param("sellerId") Long sellerId,
                   @Param("buyerId") Long buyerId
  );


}
