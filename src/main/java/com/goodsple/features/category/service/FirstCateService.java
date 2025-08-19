package com.goodsple.features.category.service;

import com.goodsple.features.category.entity.FirstCate;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FirstCateService {

    void createFirstCate(FirstCate firstCate);
    List<FirstCate> getAllFirstCate();
    FirstCate getFirstCateById(Long id);
    void updateFirstCate(FirstCate firstCate);
    void deleteFirstCate(Long id);

}
