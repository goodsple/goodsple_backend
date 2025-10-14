package com.goodsple.features.category.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class FirstCate {
    private Long firstCateId;
    private String cateName;
    private String visibility;
}
