package com.goodsple.features.category.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class SecondCate {
    private Long secondCateId;
    private String cateName;
    private Long firstCateId;
    private String subText;
    private String visibility;
}
