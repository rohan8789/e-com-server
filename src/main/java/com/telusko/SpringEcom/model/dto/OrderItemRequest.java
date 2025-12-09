package com.telusko.SpringEcom.model.dto;

import com.telusko.SpringEcom.model.OrderItem;

public record OrderItemRequest(String productId, int quantity) {
}
