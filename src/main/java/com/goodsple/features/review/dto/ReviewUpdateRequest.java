package com.goodsple.features.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class ReviewUpdateRequest {
  @NotNull
  @Min(1)
  @Max(5)
  private Integer rating;

  @NotBlank
  private String content;

  @Size(max = 5)
  private List<String> imageUrls;
}
