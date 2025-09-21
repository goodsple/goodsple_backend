package com.goodsple.features.noticelist.mapper;

import com.goodsple.features.noticelist.dto.UserNoticeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserNoticeMapper {
  List<UserNoticeDto> selectNotices(@Param("offset") int offset, @Param("size") int size);
  int countNotices();
}
