package com.goodsple.features.bookmark.mapper;

import com.goodsple.features.bookmark.entity.BookmarkFolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper // 이 인터페이스가 MyBatis Mapper임을 알려줍니다.
public interface BookmarkFolderMapper {

    // Service에서 호출할 메소드를 정의합니다.
    // 파라미터가 여러 개일 땐 @Param으로 이름을 명시해주는 게 좋아요.
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
