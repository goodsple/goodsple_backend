/**
 * 파일 경로: src/main/java/com/goodsple/features/order/entity/Order.java
 * 설명: 데이터베이스의 'orders' 테이블과 매핑되는 엔티티 클래스입니다.
 */
package com.goodsple.features.order.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor      // [추가] 빈 생성자를 만들어줍니다.
@AllArgsConstructor   // [추가] 모든 필드를 받는 생성자를 만들어줍니다.
public class Order {
    private Long orderId;
    private Long auctionId;
    private Long userId; // 낙찰자 ID
    private BigDecimal orderAmount;
    private String orderStatus;
    private OffsetDateTime orderPaymentDeadline;
}