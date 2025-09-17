package com.goodsple.features.noticelist.service.Impl;

import com.goodsple.features.noticelist.dto.UserNoticeDto;
import com.goodsple.features.noticelist.mapper.UserNoticeMapper;
import com.goodsple.features.noticelist.service.UserNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNoticeServiceImpl implements UserNoticeService {

  private final UserNoticeMapper userNoticeMapper;

  @Override
  public List<UserNoticeDto> getNotices(int page, int size) {
    int offset = (page - 1) * size;
    return userNoticeMapper.selectNotices(offset, size);
  }

  @Override
  public int getTotalCount() {
    return userNoticeMapper.countNotices();
  }
}
