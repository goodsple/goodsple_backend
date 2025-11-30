package com.goodsple.features.payment.service;

import com.goodsple.features.payment.dto.request.PaymentConfirmRequest;
import com.goodsple.features.payment.dto.response.PaymentInfoResponse;
import com.goodsple.features.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentMapper paymentMapper;
    private final RestTemplate restTemplate;

    @Value("${payment.toss.secret-key}")
    private String tossSecretKey;

    private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    @Transactional(readOnly = true)
    public PaymentInfoResponse getPaymentInfo(Long userId, Long orderId) {
        return paymentMapper.findPaymentInfoByOrderId(userId, orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public void confirmPayment(Long userId, PaymentConfirmRequest request) {

        log.info("결제 승인 요청 수신: orderId='{}', tossOrderId='{}', paymentKey='{}'",
                request.getOrderId(), request.getTossOrderId(), request.getPaymentKey());

        Long orderIdAsLong;
        try {
            orderIdAsLong = Long.valueOf(request.getOrderId());
        } catch (NumberFormatException e) {
            log.error("유효하지 않은 숫자 형식의 orderId 입니다: {}", request.getOrderId(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 주문 ID 형식입니다.");
        }

        PaymentInfoResponse orderInfo = paymentMapper.findPaymentInfoByOrderId(userId, orderIdAsLong)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문 정보가 존재하지 않습니다."));

        if (request.getAmount().compareTo(orderInfo.getAmount()) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "주문 금액이 일치하지 않습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        String encodedAuth = Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        headers.setBasicAuth(encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> bodyMap = Map.of(
                "paymentKey", request.getPaymentKey(),
                "orderId", request.getTossOrderId(),
                "amount", request.getAmount().toString()
        );

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(TOSS_CONFIRM_URL, new HttpEntity<>(bodyMap, headers), Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                paymentMapper.updateOrderStatus(orderIdAsLong, "paid");

                paymentMapper.insertShippingInfo(orderIdAsLong, request.getShippingInfo());
                log.info("주문 ID {}에 대한 배송 정보가 저장되었습니다.", orderIdAsLong);

                // TODO: 6-3. payments 테이블에 결제 기록 저장 (필요 시 추가)

            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "결제 승인에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("토스페이먼츠 승인 API 호출 중 예외 발생", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "결제 승인 중 오류가 발생했습니다.");
        }
    }
}