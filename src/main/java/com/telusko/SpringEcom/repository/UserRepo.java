package com.telusko.SpringEcom.repository;

import com.telusko.SpringEcom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository

public interface UserRepo extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.emailId=:email")
    User findByEmail(String email);
}
