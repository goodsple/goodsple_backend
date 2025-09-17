package com.goodsple.features.bookmark.mapper;

import com.goodsple.features.bookmark.entity.Bookmark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BookmarkMapper {

    void save(Bookmark bookmark);

    List<Bookmark> findByUserId(@Param("userId") Long userId);

    void delete(@Param("bookmarkId") Long bookmarkId);

    boolean exists(Map<String, Object> params);

    List<Bookmark> findByUserIdAndFolderId(Map<String, Object> params);

    List<Bookmark> findByFolderId(@Param("folderId") Long folderId);

    void updateFolder(Map<String, Object> params);

    int countByExchangePostId(@Param("postId") Long postId);

    int countByEventPostId(@Param("postId") Long postId);

    int deleteByUserAndBookmarkId(Map<String, Object> params);

}
