/**
 * 파일 경로: src/main/java/com/goodsple/features/auction/dto/response/WinnerInfo.java
 * 설명: 낙찰자 정보를 담는 서브 DTO입니다.
 */
package com.goodsple.features.auction.dto.response;

import lombok.Data;

@Data
public class WinnerInfo {
    private Long userId;
    private String nickname;
    private String phone;
}