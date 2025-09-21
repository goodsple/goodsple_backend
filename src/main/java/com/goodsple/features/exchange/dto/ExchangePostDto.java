package com.goodsple.features.exchange.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangePostDto {

  private Long exchangePostId;

  // 추가된 필드: 3차 카테고리 ID
  @NotNull(message = "카테고리 선택은 필수입니다.")
  private Long thirdCateId;

  @NotBlank(message = "상품명은 필수입니다.")
  @Size(max = 40, message = "상품명은 40자를 초과할 수 없습니다.")
  private String exchangePostTitle;

  @NotBlank(message = "게시글 내용은 필수입니다.")
  private String postDescription;

  // 등록 시에만 사용되는 필드
  @NotBlank(message = "게시글 지역 코드는 필수입니다.")
  private String postLocationCode;

  @NotBlank(message = "게시글 지역 이름은 필수입니다.")
  private String postLocationName;

  private String postHopeRegion; // 직거래 희망 장소 (선택 사항)

  @NotBlank(message = "거래 방식은 필수입니다.")
  @Size(max = 10, message = "거래 방식은 10자를 초과할 수 없습니다.")
  private String postTradeType; // DIRECT, DELIVERY, BOTH 중 하나

  private Integer deliveryPriceNormal; // 일반 택배비 (택배 거래 시 필수)

  private String halfDeliveryType; // 반값택배 상세 옵션(둘다 가능, GS25만 가능, CU만 가능)
  private Integer deliveryPriceHalf; // 반값 택배비 (선택 사항)

  @NotNull(message = "이미지 URL은 하나 이상 필요합니다.")
//  @Size(min = 1, message = "이미지 URL은 최소 하나 이상 필요합니다.")
  private List<String> imageUrls;



}
