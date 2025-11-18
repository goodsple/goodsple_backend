package com.goodsple.features.admin.notice.mapper;

import com.goodsple.features.admin.notice.dto.NoticeListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeListMapper {

  List<NoticeListDto> selectNoticeList(
      @Param("title") String title,
      @Param("isPopup") Boolean isPopup,
      @Param("offset") int offset,
      @Param("size") int size
  );

  int selectNoticeListCount(
      @Param("title") String title,
      @Param("isPopup") Boolean isPopup
  );

}
