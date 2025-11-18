package com.goodsple.features.category.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class ThirdCate {
    private Long thirdCateId;
    private String cateName;
    private Long secondCateId;
    private String subText;
    private String visibility;

}
