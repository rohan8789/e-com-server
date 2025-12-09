package com.telusko.SpringEcom.controller;


import com.telusko.SpringEcom.model.Order;
import com.telusko.SpringEcom.model.OrderItem;
import com.telusko.SpringEcom.model.dto.OrderRequest;
import com.telusko.SpringEcom.model.dto.OrderResponse;
import com.telusko.SpringEcom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrderResponse(){
        List<OrderResponse>l=orderService.getAllOrders();
        return new ResponseEntity<>(l, HttpStatus.OK);
    }

    @PostMapping("/orders/place")
    public ResponseEntity<OrderResponse> placeOrder( @RequestBody OrderRequest orderReq){

        //create Order
        OrderResponse orderResponse = orderService.createOrder(orderReq);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
