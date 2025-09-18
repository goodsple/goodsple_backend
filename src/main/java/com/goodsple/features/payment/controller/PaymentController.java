package com.goodsple.features.payment.controller;

import com.goodsple.features.payment.dto.request.PaymentConfirmRequest;
import com.goodsple.features.payment.dto.response.PaymentInfoResponse;
import com.goodsple.features.payment.service.PaymentService;
import com.goodsple.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "06. 결제 (Payment)", description = "결제 관련 API")
@RestController
@RequestMapping("/api") // 공통 경로 사용
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 페이지 정보 조회", description = "결제 페이지에 필요한 주문 정보를 조회합니다.")
    @GetMapping("/orders/{orderId}/payment-info")
    public ResponseEntity<PaymentInfoResponse> getPaymentInfo(
            Authentication authentication,
            @PathVariable Long orderId) {

        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        PaymentInfoResponse response = paymentService.getPaymentInfo(userId, orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "결제 승인", description = "토스페이먼츠 결제를 최종 승인하고 서버에 기록합니다.")
    @PostMapping("/payments/confirm")
    public ResponseEntity<Void> confirmPayment(
            Authentication authentication,
            @Valid @RequestBody PaymentConfirmRequest request) {

        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        paymentService.confirmPayment(userId, request);
        return ResponseEntity.ok().build();
    }
}