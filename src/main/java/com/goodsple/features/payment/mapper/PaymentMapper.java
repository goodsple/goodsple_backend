package com.goodsple.features.payment.mapper;

import com.goodsple.features.payment.dto.request.ShippingInfoRequest;
import com.goodsple.features.payment.dto.response.PaymentInfoResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Optional;

@Mapper
public interface PaymentMapper {
    Optional<PaymentInfoResponse> findPaymentInfoByOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);

    void updateOrderStatus(@Param("orderId") Long orderId, @Param("status") String status);

    void insertShippingInfo(@Param("orderId") Long orderId, @Param("info") ShippingInfoRequest shippingInfo);
}