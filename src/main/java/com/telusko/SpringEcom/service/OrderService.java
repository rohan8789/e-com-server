package com.telusko.SpringEcom.service;

import com.telusko.SpringEcom.model.Order;
import com.telusko.SpringEcom.model.OrderItem;
import com.telusko.SpringEcom.model.Product;
import com.telusko.SpringEcom.model.dto.OrderItemRequest;
import com.telusko.SpringEcom.model.dto.OrderItemResponse;
import com.telusko.SpringEcom.model.dto.OrderRequest;
import com.telusko.SpringEcom.model.dto.OrderResponse;
import com.telusko.SpringEcom.repository.OrderRepo;
import com.telusko.SpringEcom.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    ProductRepo productRepo;
    @Autowired
    OrderRepo orderRepo;

    public OrderResponse createOrder(@RequestBody OrderRequest orderReq){

        List<OrderItem> orderItems = new ArrayList<>();

        Order order = new Order();
        String o_id = "orderID" + UUID.randomUUID().toString().substring(0,9);
        order.setOrderId(o_id);
        order.setOrderStatus("PLACED");
        order.setCustName(orderReq.customerName());
        order.setEmail(orderReq.email());
        order.setOrderDate(LocalDate.now());

        for(OrderItemRequest i:orderReq.items()){
            int id= Integer.parseInt(i.productId());
            Product findProd = productRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));


                if(findProd.isProductAvailable()){
                    int quant = findProd.getStockQuantity()-i.quantity();
                    if(quant>=0){
                        findProd.setStockQuantity(findProd.getStockQuantity()-i.quantity());
                        productRepo.save(findProd);
                        OrderItem orderItem = new OrderItem();
                        orderItem.setQuantity(i.quantity());
                        orderItem.setProduct(findProd);
                        orderItem.setTotalPrice(findProd.getPrice().multiply(BigDecimal.valueOf(i.quantity())));
                        orderItem.setOrder(order);
                        orderItems.add(orderItem);
                    }
                }

        }

        order.setItems(orderItems);
        Order savedOrder=orderRepo.save(order);

        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for(OrderItem item: savedOrder.getItems()){
            OrderItemResponse res = new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            );
        }

        //Response
        OrderResponse orderResponse = new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustName(),
                savedOrder.getEmail(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderDate(),
                orderItemResponses
        );

        return orderResponse;
    }


    @Transactional
    public List<OrderResponse> getAllOrders(){

        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {


            List<OrderItemResponse> itemResponses = new ArrayList<>();

            for(OrderItem item : order.getItems()) {
                OrderItemResponse orderItemResponse = new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getTotalPrice()
                );
                itemResponses.add(orderItemResponse);

            }
            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustName(),
                    order.getEmail(),
                    order.getOrderStatus(),
                    order.getOrderDate(),
                    itemResponses
            );
            orderResponses.add(orderResponse);
        }
        
        return orderResponses;
    }
}
