package com.goodsple.features.category.service;

import com.goodsple.features.category.entity.SecondCate;
import com.goodsple.features.category.mapper.SecondCateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecondCateServiceImpl implements SecondCateService{

    private final SecondCateMapper mapper;

    @Override
    @Transactional
    public SecondCate createSecondCate(SecondCate secondCate) {
        mapper.insertCate(secondCate);
        return secondCate;
    }

    @Override
    @Transactional
    public void updateSecondCate(SecondCate secondCate) {
        mapper.updateCate(secondCate);
    }

    @Override
    @Transactional
    public void deleteSecondCate(Long id) {
        mapper.deleteCateById(id);
    }

    @Override
    public List<SecondCate> getAllSecondCate() {
        return mapper.getAllSecondCate();
    }

    @Override
    public List<SecondCate> getAllSecondCateByFirstCateId(Long firstCateId) {
        return mapper.getAllSecondCateByFirstCateId(firstCateId);
    }

    @Override
    public SecondCate getSecondCateById(Long id) {
        return mapper.getSecondCateById(id);
    }
}