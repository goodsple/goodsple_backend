package com.goodsple.features.myexchange.dto;

import lombok.Data;

@Data
public class MyCompletedExchangeDto {

  private Long exchangePostId;
  private String title;
  private String imageUrl;

  private String tradeMethod;
  private String tradedAt;

  private Long sellerId;
  private Long buyerId;

  private String opponentNickname;

  private boolean isSeller;        // 내가 판매자인지
  private boolean isBuyer;         // 내가 구매자인지

  private boolean canSelectBuyer;  // 거래상대 지정 버튼
  private boolean canWriteReview;  // 후기 작성 가능
  private boolean reviewWritten;   // 리뷰 작성 완료 여부

}
