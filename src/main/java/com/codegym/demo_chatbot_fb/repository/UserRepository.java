package com.codegym.demo_chatbot_fb.repository;

import com.codegym.demo_chatbot_fb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("select u from User u where u.status = true")
    Iterable<User> findAllByStatusIsTrue();

    @Query("select u from User u where u.status = false ")
    Iterable<User> findAllByStatusIsFalse();
}
