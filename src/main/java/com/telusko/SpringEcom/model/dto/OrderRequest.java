package com.telusko.SpringEcom.model.dto;

import com.telusko.SpringEcom.model.OrderItem;

import java.util.List;

public record OrderRequest(String customerName, String email, List<OrderItemRequest> items) {


}
