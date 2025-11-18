package com.goodsple.features.category.service;

import com.goodsple.features.category.entity.ThirdCate;

import java.util.List;

public interface ThirdCateService {

    ThirdCate createThirdCate(ThirdCate thirdCate);
    void updateThirdCate(ThirdCate thirdCate);
    void deleteThirdCate(Long id);

    List<ThirdCate> getAllThirdCate();
    List<ThirdCate> getAllThirdCateBySecondCateId(Long secondCateId);
    ThirdCate getThirdCateById(Long id);
}
