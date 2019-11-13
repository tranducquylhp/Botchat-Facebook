package com.codegym.demo_chatbot_fb.service.impl;

import com.codegym.demo_chatbot_fb.model.User;
import com.codegym.demo_chatbot_fb.repository.UserRepository;
import com.codegym.demo_chatbot_fb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(String id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).get();
    }

    @Override
    public Iterable<User> findAllByStatusIsTrue() {
        return userRepository.findAllByStatusIsTrue();
    }

    @Override
    public Iterable<User> findAllByStatusIsFalse() {
        return userRepository.findAllByStatusIsFalse();
    }


}
