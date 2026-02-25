package com.goodsple.features.myexchange.service.Impl;

import com.goodsple.features.badge.service.BadgeService;
import com.goodsple.features.myexchange.dto.ChatUserResponseDto;
import com.goodsple.features.myexchange.dto.MyCompletedExchangeDto;
import com.goodsple.features.myexchange.dto.MyExchangePostDto;
import com.goodsple.features.myexchange.dto.MyExchangePostUpdateDto;
import com.goodsple.features.myexchange.mapper.MyExchangePostMapper;
import com.goodsple.features.myexchange.service.MyExchangePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyExchangePostServiceImpl implements MyExchangePostService {

  private final MyExchangePostMapper myExchangePostMapper;
  private final BadgeService badgeService;

  // 내가 작성한 교환 게시글 목록 조회 (거래상태 필터링, 페이징처리)
  @Override
  public List<MyExchangePostDto> getMyPosts(Long userId, String status, int page, int size) {
    int offset = (page - 1) * size;
    return myExchangePostMapper.selectMyExchangePosts(userId, status, offset, size);
  }

  // 내가 작성한 교환게시글 전체 개수 조회
  @Override
  public int getMyPostsCount(Long userId, String status) {
    return myExchangePostMapper.countMyExchangePosts(userId, status);
  }

  // 교환게시글 거래 상태 변경 (작성자만 변경 가능)
  @Override
  public void updatePostStatus(Long postId, Long userId, String status) {

    // 게시글 상태 업데이트
    int updated = myExchangePostMapper.updatePostStatus(postId, userId, status);

    // 업데이트 결과 검증
    // 게시글이 없거나 , 작성자가 아닌 경우- 변경 실패  시 예외 발생
    if (updated != 1) {
      throw new RuntimeException("거래상태 업데이트 실패 또는 권한 없음");
    }
  }

  // 특정 거래글에 대해 채팅을 진행한 사용자 조회 (거래완료 후 거래 상대방 지정 용도)
  @Override
  public List<ChatUserResponseDto> getChatUsers(Long postId, Long sellerId) {

    // 권한 체크: 현재 사용자가 이 글의 판매자인지
    boolean isOwner = myExchangePostMapper.isPostOwner(postId, sellerId);
    if (!isOwner) {
      throw new RuntimeException("권한 없음");
    }

    // sellerId = 현재 로그인한 사용자 = currentUserId
    // 해당 게시글에 대해 채팅한 사용자 목록 조회
    return myExchangePostMapper.selectChatUsersByPostId(postId, sellerId);
  }

  // 거래완료된 게시글에 대해 실제 거래 상대 지정
  // 지정된 구매자만 후기 작성 가능
  @Override
  public void selectBuyer(Long postId, Long sellerId, Long buyerId) {

    // 1. 검증 - 본인을 거래상대로 선택하는 경우 방지
    if (sellerId.equals(buyerId)) {
      throw new IllegalArgumentException("본인을 거래상대로 선택할 수 없습니다.");
    }

    // 2. DB 업데이트
    int updated = myExchangePostMapper.updateBuyer(postId, sellerId, buyerId);

    // 3. 결과 검증 (게시글이 없거나, 판매자가 아니거나, 이미 거래상대가 지정된 경우 등)
    if (updated != 1) {
      throw new RuntimeException("거래상대 지정 실패 또는 권한 없음");
    }

    boolean fast = checkFastResponse(postId, sellerId);

    badgeService.rewardTradeComplete(sellerId, postId, fast);
    badgeService.rewardTradeComplete(buyerId, postId, fast);

  }

  private boolean checkFastResponse(Long postId, Long userId){

    LocalDateTime created = myExchangePostMapper.findFirstChatTime(postId, userId);

    LocalDateTime completed = myExchangePostMapper.findCompletedTime(postId);

    if(created == null || completed == null){
        return false;
    }

    long days = Duration.between(created, completed).toDays();

    return days <= 3;

  }


  // 내가 참여한 거래완료 내역 조회 (내가 판매자/구매자인 거래 모두)
  // 판매자/구매자 여부, 후기 작성 가능 여부, 후기 작성 여부
  @Override
  public List<MyCompletedExchangeDto> getMyCompletedExchangeHistory(Long userId) {
    return myExchangePostMapper.selectMyCompletedExchangeHistory(userId);
  }





}
