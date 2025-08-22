package com.goodsple.features.bookmark.mapper;

import com.goodsple.features.bookmark.entity.Bookmark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookmarkMapper {

    void save(@Param("userId") Long userId,
              @Param("folderId") Long folderId,
              @Param("exchangePostId") Long exchangePostId,
              @Param("eventPostId") Long eventPostId);

    List<Bookmark> findByUserId(@Param("userId") Long userId);

    void delete(@Param("bookmarkId") Long bookmarkId);

    boolean exists(@Param("userId") Long userId,
                   @Param("exchangePostId") Long exchangePostId,
                   @Param("eventPostId") Long eventPostId);
}
