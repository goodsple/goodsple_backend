package com.goodsple.features.bookmark.mapper;

import com.goodsple.features.bookmark.entity.BookmarkFolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookmarkFolderMapper {

    void save(@Param("folderName") String folderName,
              @Param("folderColor") String folderColor,
              @Param("userId") Long userId);

    Long getLastInsertId();

    void update(@Param("folderId") Long folderId,
                @Param("folderName") String folderName,
                @Param("folderColor") String folderColor,
                @Param("userId") Long userId);

    void delete(@Param("folderId") Long folderId,
                @Param("userId") Long userId);

    List<BookmarkFolder> findByUserId(@Param("userId") Long userId);
}
