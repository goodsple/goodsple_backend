package com.goodsple.features.myexchange.mapper;

import com.goodsple.features.myexchange.dto.ChatUserResponseDto;
import com.goodsple.features.myexchange.dto.MyCompletedExchangeDto;
import com.goodsple.features.myexchange.dto.MyExchangePostDto;
import com.goodsple.features.myexchange.dto.MyExchangePostUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MyExchangePostMapper {

  // 내 거래글 목록 조회
  List<MyExchangePostDto> selectMyExchangePosts(
      @Param("userId") Long userId,   // 로그인 유저 ID (판매자)
      @Param("status") String status, // 거래상태 필터 (AVAILABLE / ONGOING / COMPLETED)
      @Param("offset") int offset,
      @Param("size") int size
  );

  // 거래상태 필터 (AVAILABLE / ONGOING / COMPLETED
  int countMyExchangePosts(
      @Param("userId") Long userId,
      @Param("status") String status
  );

  // 거래상태 변경(업데이트)
  // 상태값: AVAILABLE / ONGOING / COMPLETED
  int updatePostStatus(@Param("postId") Long postId,
                       @Param("userId") Long userId,
                       @Param("status") String status); // 변경할 거래상태

  // 거래글 판매자 여부 확인
  // true: 판매자 / false: 권한없음
  boolean isPostOwner(@Param("postId") Long postId,   // 거래글 ID
                      @Param("userId") Long userId);  // 로그인 유저 ID


  // 거래글에 연결된 채팅 상대 목록 조회
  // 판매자 본인 제외
  List<ChatUserResponseDto> selectChatUsersByPostId (@Param("postId") Long postId,
                                                    @Param("currentUserId") Long currentUserId);



  // 거래상대(구매자) 확정
  // 거래완료 상태에서만 호출
  // 거래글 당 1명의 구매자만 지정 가능(본인 선택 불가)
  int updateBuyer (@Param("postId") Long postId,
                   @Param("sellerId") Long sellerId,
                   @Param("buyerId") Long buyerId
  );

  // 내가 참여한 거래완료 내역 조회 (내가 판매자/구매자인 거래 모두)
  List<MyCompletedExchangeDto> selectMyCompletedExchangeHistory(
      @Param("userId") Long userId
  );

}
