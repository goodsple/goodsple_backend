package com.goodsple.features.noticelist.service.Impl;

import com.goodsple.features.noticelist.dto.UserNoticeDetailDto;
import com.goodsple.features.noticelist.mapper.UserNoticeDetailMapper;
import com.goodsple.features.noticelist.service.UserNoticeDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNoticeDetailServiceImpl implements UserNoticeDetailService {

  private final UserNoticeDetailMapper userNoticeDetailMapper;

  @Override
  public UserNoticeDetailDto getNotice(Long noticeId) {
    return userNoticeDetailMapper.selectNoticeById(noticeId);
  }

}
