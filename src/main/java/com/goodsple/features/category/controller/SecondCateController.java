package com.goodsple.features.category.controller;

import com.goodsple.features.category.entity.SecondCate;
import com.goodsple.features.category.service.SecondCateService;
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
@RequestMapping("/api/admin/category/second")
@RequiredArgsConstructor
@Tag(name="SecondCate", description="2차 카테고리")
public class SecondCateController {

    private final SecondCateService secondCateService;

    @PostMapping
    public ResponseEntity<SecondCate> create(@RequestBody SecondCate secondCate) {
        SecondCate saved = secondCateService.createSecondCate(secondCate);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody SecondCate secondCate) {
        secondCate.setSecondCateId(id);
        secondCateService.updateSecondCate(secondCate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        secondCateService.deleteSecondCate(id);
        return ResponseEntity.noContent().build();
    }

    // 2차 카테 전체조회
    @Operation(summary = "2차 카테고리 전체조회", description = "2차 카테고리 전체를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "2차 카테고리 전체 목록이 성공적으로 조회되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.")
    })
    @GetMapping("/all")
    public ResponseEntity<List<SecondCate>> getAllSecondCates()
    {
        List<SecondCate> cates = secondCateService.getAllSecondCate();
        return ResponseEntity.ok(cates);
    }

}



