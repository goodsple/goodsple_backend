/**
 * 파일 경로: src/main/java/com/goodsple/features/order/entity/Order.java
 * 설명: 데이터베이스의 'orders' 테이블과 매핑되는 엔티티 클래스입니다.
 */
package com.goodsple.features.order.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class Order {
    private Long orderId;
    private Long auctionId;
    private Long userId; // 낙찰자 ID
    private BigDecimal orderAmount;
    private String orderStatus;
}