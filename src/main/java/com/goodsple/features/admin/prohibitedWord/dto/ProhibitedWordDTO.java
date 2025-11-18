package com.goodsple.features.admin.prohibitedWord.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class ProhibitedWordDTO {
    private Long wordId;
    private String word;
    private Boolean wordIsActive;
    private OffsetDateTime wordCreatedAt;
}
