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

    private final SecondCateMapper secondCateMapper;

//    @Override
//    @Transactional
//    public void createSecondCate(SecondCate secondCate) {
//        secondCateMapper.insertCate(secondCate);
//    }

    @Override
    @Transactional
    public SecondCate createSecondCate(SecondCate secondCate) {
        secondCateMapper.insertCate(secondCate);
        return secondCate; // DB 저장 후 ID가 들어간 객체 반환
    }


    @Override
    public List<SecondCate> getAllSecondCate() {
        return secondCateMapper.getAllSecondCate();
    }

    @Override
    public List<SecondCate> getAllSecondCateByFirstCateId(Long id) {
        return secondCateMapper.getAllSecondCateByFirstCateId(id);
    }

    @Override
    public SecondCate getSecondCateById(Long id) {
        return secondCateMapper.getSecondCateById(id);
    }

    @Override
    @Transactional
    public void updateSecondCate(SecondCate secondCate)
    {
        secondCateMapper.updateCate(secondCate);
    }

    @Override
    public void deleteSecondCate(Long id) {
        secondCateMapper.deleteCateById(id);
    }

}
