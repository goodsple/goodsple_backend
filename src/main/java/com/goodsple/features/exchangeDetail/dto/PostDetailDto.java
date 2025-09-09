package com.goodsple.features.exchangeDetail.dto;

import com.goodsple.features.user.dto.request.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto {

  private Long postId;
  private String title;
  private String category;
  private String description;
  private String status;
  private Long writerId;
  private String location;
  private String tradeType;
  private DeliveryInfo delivery;
  private List<String> images;
  private PostWriterDto writer;
  private OffsetDateTime createdAt;

  private Boolean isWriter;
//  private int likes;
//  private int views;
//  private int queueCount;


}
