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
    @Transactional
    public Long createFolder(FolderCreationRequest request, Long userId) {
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