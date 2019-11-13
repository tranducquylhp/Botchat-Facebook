package com.codegym.demo_chatbot_fb.repository;

import com.codegym.demo_chatbot_fb.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u where u.status = true")
    Iterable<User> findAllByStatusIsTrue();

    @Query("SELECT u FROM User u where u.status = false ")
    Iterable<User> findAllByStatusIsFalse();
}
