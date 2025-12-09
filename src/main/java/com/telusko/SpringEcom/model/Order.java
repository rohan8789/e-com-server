package com.telusko.SpringEcom.model;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity(name="orders")

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique=true)
    private String OrderId;

    private String custName;
    private String email;
    private String orderStatus;
    private LocalDate orderDate;

    //One Order ----> Many Items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
