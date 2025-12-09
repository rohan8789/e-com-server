package com.telusko.SpringEcom.model.dto;

import java.math.BigDecimal;

public record OrderItemResponse(String ProdName, int quantity, BigDecimal totalPrice) {
}
