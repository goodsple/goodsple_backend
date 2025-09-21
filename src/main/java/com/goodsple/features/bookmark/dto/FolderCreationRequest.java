package com.goodsple.features.bookmark.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FolderCreationRequest {

    @NotBlank(message = "폴더 이름은 필수입니다.")
    @Size(max = 20, message = "폴더 이름은 20자 인하로 입력해주세요.")
    private String folderName;

    @NotBlank(message = "폴더 색상을 선택해주세요.")
    private String folderColor;
}
