package com.goodsple.features.admin.prohibitedWord.mapper;

import com.goodsple.features.admin.prohibitedWord.dto.ProhibitedWordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProhibitedWordMapper {

    List<ProhibitedWordDTO> selectAllWords();

    int insertWord(@Param("word") String word);

    int deleteWords(@Param("ids") List<Long> ids);

    int countByWord(@Param("word") String word);

    int toggleWordActive(@Param("id") Long id);

    List<String> selectActiveWords();


}