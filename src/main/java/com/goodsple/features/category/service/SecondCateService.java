package com.goodsple.features.category.service;

import com.goodsple.features.category.entity.SecondCate;

import java.util.List;

public interface SecondCateService {

    void createSecondCate(SecondCate SecondCate);
    List<SecondCate> getAllSecondCate();
    List<SecondCate> getAllSecondCateByFirstCateId(Long id);
    SecondCate getSecondCateById(Long id);
    void updateSecondCate(SecondCate SecondCate);
    void deleteSecondCate(Long id);

}
