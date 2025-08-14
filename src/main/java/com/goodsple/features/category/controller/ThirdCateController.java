package com.goodsple.features.category.controller;

import com.goodsple.features.category.entity.ThirdCate;
import com.goodsple.features.category.service.ThirdCateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category/third")
@RequiredArgsConstructor
@Tag(name="ThirdCate", description="3차 카테고리")
public class ThirdCateController {
    
    private final ThirdCateService thirdCateService;

    // 3차 카테 등록
    @Operation(summary = "3차 카테고리 등록", description = "3차 카테고리를 등록합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "3차 카테고리가 성공적으로 등록되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "401", description = "인증 오류입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.")
    })
    @PostMapping
    public ResponseEntity<Void> createThirdCate(
            @RequestBody ThirdCate ThirdCate)
    {
        thirdCateService.createThirdCate(ThirdCate);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 3차 카테 전체조회
    @Operation(summary = "3차 카테고리 전체조회", description = "3차 카테고리 전체를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "3차 카테고리 전체 목록이 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ThirdCate>> getAllThirdCates()
    {
        List<ThirdCate> cates = thirdCateService.getAllThirdCate();
        return ResponseEntity.ok(cates);
    }

    @Operation(summary = "3차 카테고리 부분조회", description = "3차 카테고리 일부를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "3차 카테고리 일부 목록이 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.")
    })
    @GetMapping("/{secondCateId}")
    public ResponseEntity<List<ThirdCate>> getAllThirdCatesBySecondCateId(@PathVariable Long secondCateId)
    {
        List<ThirdCate> cates = thirdCateService.getAllThirdCateBySecondCateId(secondCateId);
        return ResponseEntity.ok(cates);
    }
    
}
