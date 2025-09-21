package com.goodsple.features.category.service;

import com.goodsple.features.category.entity.ThirdCate;

import java.util.List;

public interface ThirdCateService {

    void createThirdCate(ThirdCate ThirdCate);
    List<ThirdCate> getAllThirdCate();
    List<ThirdCate> getAllThirdCateBySecondCateId(Long id);
    ThirdCate getThirdCateById(Long id);
    void updateThirdCate(ThirdCate ThirdCate);
    void deleteThirdCate(Long id);

}
