package com.goodsple.features.noticelist.mapper;

import com.goodsple.features.noticelist.dto.UserNoticeDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserNoticeDetailMapper {

  UserNoticeDetailDto selectNoticeById(@Param("noticeId") Long noticeId);

}
