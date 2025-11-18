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
    public ThirdCate createThirdCate(ThirdCate thirdCate) {
        thirdCateMapper.insertCate(thirdCate);
        return thirdCate;
    }

    @Override
    @Transactional
    public void updateThirdCate(ThirdCate thirdCate) {
        thirdCateMapper.updateCate(thirdCate);
    }

    @Override
    @Transactional
    public void deleteThirdCate(Long id) {
        thirdCateMapper.deleteCateById(id);
    }

    @Override
    public List<ThirdCate> getAllThirdCate() {
        return thirdCateMapper.getAllThirdCate();
    }

    @Override
    public List<ThirdCate> getAllThirdCateBySecondCateId(Long secondCateId) {
        return thirdCateMapper.getAllThirdCateBySecondCateId(secondCateId);
    }

    @Override
    public ThirdCate getThirdCateById(Long id) {
        return thirdCateMapper.getThirdCateById(id);
    }
}
