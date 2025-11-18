package com.goodsple.features.category.service;

import com.goodsple.features.category.entity.SecondCate;

import java.util.List;

public interface SecondCateService {

    SecondCate createSecondCate(SecondCate secondCate);
    void updateSecondCate(SecondCate secondCate);
    void deleteSecondCate(Long id);

    List<SecondCate> getAllSecondCate();
    List<SecondCate> getAllSecondCateByFirstCateId(Long firstCateId);
    SecondCate getSecondCateById(Long id);
}
