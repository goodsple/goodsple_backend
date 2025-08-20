package com.goodsple.features.bookmark.service;

import com.goodsple.features.bookmark.dto.FolderCreationRequest;
import com.goodsple.features.bookmark.entity.BookmarkFolder;
import com.goodsple.features.bookmark.mapper.BookmarkFolderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookmarkFolderService {

    private final BookmarkFolderMapper bookmarkFolderMapper;

    public BookmarkFolderService(BookmarkFolderMapper bookmarkFolderMapper) {
        this.bookmarkFolderMapper = bookmarkFolderMapper;
    }

    // 폴더 조회
    public List<BookmarkFolder> getFoldersByUserId(Long userId) {
        return bookmarkFolderMapper.findByUserId(userId);
    }

    // 폴더 생성
    @Transactional // 이 메소드 내의 DB 작업들이 하나의 단위로 묶여요. 중간에 실패하면 모두 롤백!
    public Long createFolder(FolderCreationRequest request, Long userId) {
        // 나중에는 여기에 폴더 개수 제한, 이름 중복 검사 등의 로직을 추가할 수 있어요.

        // DB에 저장하기 위해 Mapper를 호출해요.
        // DTO에 있는 정보와 유저 ID를 넘겨줍니다.
        bookmarkFolderMapper.save(request.getFolderName(), request.getFolderColor(), userId);

        Long folderId = bookmarkFolderMapper.getLastInsertId();

        return folderId;
    }

    // 폴더 수정
    @Transactional
    public void editFolder(Long folderId, FolderCreationRequest request, Long userId) {
        bookmarkFolderMapper.update(folderId, request.getFolderName(), request.getFolderColor(), userId);
    }

    // 폴더 삭제
    @Transactional
    public void deleteFolder(Long folderId, Long userId) {
        bookmarkFolderMapper.delete(folderId, userId);
    }

}