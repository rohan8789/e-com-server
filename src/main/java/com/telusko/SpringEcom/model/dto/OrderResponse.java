package com.telusko.SpringEcom.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public record OrderResponse(
        String orderId,
        String custName,
        String email,
        String orderStatus,
        LocalDate orderDate,
        List<OrderItemResponse> items
) {
}
