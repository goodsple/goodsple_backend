package com.goodsple.features.notice.mapper;

import com.goodsple.features.notice.dto.NoticeListDto;
import com.goodsple.features.notice.dto.NoticeListFilterDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeListMapper {

  List<NoticeListDto> selectNoticeList(@Param("filter") NoticeListFilterDto filter);

  int countNoticeList(@Param("filter") NoticeListFilterDto filter);

  NoticeListDto selectNoticeDetail(@Param("noticeId") Long noticeId);

}
