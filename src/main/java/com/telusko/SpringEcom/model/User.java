package com.telusko.SpringEcom.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users") //user is reserved keyword in postgres
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userId;
    private String name;

    @Column(unique = true)
    private String emailId;
    private String password;

    //One User Many Order
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Order> orders;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
