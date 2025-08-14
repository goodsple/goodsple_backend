package com.goodsple.features.category.service;

import com.goodsple.features.category.entity.ThirdCate;
import com.goodsple.features.category.mapper.ThirdCateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThirdCateServiceImpl implements ThirdCateService {

    private final ThirdCateMapper thirdCateMapper;

    @Override
    @Transactional
    public void createThirdCate(ThirdCate ThirdCate) {
        thirdCateMapper.insertCate(ThirdCate);
    }

    @Override
    public List<ThirdCate> getAllThirdCate() {
        return thirdCateMapper.getAllThirdCate();
    }

    @Override
    public List<ThirdCate> getAllThirdCateBySecondCateId(Long id) {
        return thirdCateMapper.getAllThirdCateBySecondCateId(id);
    }

    @Override
    public ThirdCate getThirdCateById(Long id) {
        return thirdCateMapper.getThirdCateById(id);
    }

    @Override
    @Transactional
    public void updateThirdCate(ThirdCate ThirdCate) {
        thirdCateMapper.updateCate(ThirdCate);
    }

    @Override
    @Transactional
    public void deleteThirdCate(Long id) {
        thirdCateMapper.deleteCateById(id);
    }
}
