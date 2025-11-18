package com.goodsple.features.category.controller;

import com.goodsple.features.category.entity.ThirdCate;
import com.goodsple.features.category.service.SecondCateService;
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


    @PostMapping
    public ResponseEntity<ThirdCate> create(@RequestBody ThirdCate thirdCate) {
        ThirdCate saved = thirdCateService.createThirdCate(thirdCate);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ThirdCate thirdCate) {
        thirdCate.setThirdCateId(id);
        thirdCateService.updateThirdCate(thirdCate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        thirdCateService.deleteThirdCate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ThirdCate>> getAll() {
        return ResponseEntity.ok(thirdCateService.getAllThirdCate());
    }

    @GetMapping("/{secondCateId}")
    public ResponseEntity<List<ThirdCate>> getBySecondCate(@PathVariable Long secondCateId) {
        return ResponseEntity.ok(thirdCateService.getAllThirdCateBySecondCateId(secondCateId));
    }
}
