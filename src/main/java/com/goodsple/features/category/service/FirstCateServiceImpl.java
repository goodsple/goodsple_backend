package com.goodsple.features.category.service;

import com.goodsple.features.category.entity.FirstCate;
import com.goodsple.features.category.mapper.FirstCateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FirstCateServiceImpl implements FirstCateService{
    private final FirstCateMapper firstCateMapper;

    @Override
    @Transactional
    public void createFirstCate(FirstCate firstCate) {
        firstCateMapper.insertCate(firstCate);
    }

    @Override
    public List<FirstCate> getAllFirstCate() {
        return firstCateMapper.getAllFirstCate();
    }

    @Override
    public FirstCate getFirstCateById(Long id) {
        return firstCateMapper.getFirstCateById(id);
    }

    @Override
    @Transactional
    public void updateFirstCate(FirstCate firstCate)
    {
        firstCateMapper.updateCate(firstCate);
    }

    @Override
    public void deleteFirstCate(Long id) {
        firstCateMapper.deleteCateById(id);
    }

}
