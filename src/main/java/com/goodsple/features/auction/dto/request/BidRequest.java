package com.goodsple.features.auction.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BidRequest {
    private BigDecimal amount;
}
