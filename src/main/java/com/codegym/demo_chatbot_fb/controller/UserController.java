package com.codegym.demo_chatbot_fb.controller;

import com.codegym.demo_chatbot_fb.model.User;
import com.codegym.demo_chatbot_fb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ModelAndView listUsers(Pageable pageable){
        ModelAndView modelAndView = new ModelAndView("user/list");
        Page<User> users = userService.findAll(pageable);
        modelAndView.addObject("users",users);
        return modelAndView;
    }

    @GetMapping("/user/delete/{id}")
    public ModelAndView deleteUserForm(@PathVariable String id){
        ModelAndView modelAndView = new ModelAndView("user/delete");
        Optional<User> user = userService.findById(id);
        if (user.isPresent()){
            modelAndView.addObject("user",user.get());
        } else modelAndView = new ModelAndView("error");
        return modelAndView;
    }

    @PostMapping("/user/delete/{id}")
    public ModelAndView deleteUser(@PathVariable String id){
        ModelAndView modelAndView = new ModelAndView("redirect:/users");
        userService.delete(id);
        return modelAndView;
    }

    @GetMapping("/users/statusTrue")
    public ModelAndView listUsersTrue(){
        ModelAndView modelAndView = new ModelAndView("user/listTrue");
        Iterable<User> users = userService.findAllByStatusIsTrue();
        modelAndView.addObject("users",users);
        return modelAndView;
    }

    @GetMapping("/users/statusFalse")
    public ModelAndView listUsersFalse(){
        ModelAndView modelAndView = new ModelAndView("user/listFalse");
        Iterable<User> users = userService.findAllByStatusIsFalse();
        modelAndView.addObject("users",users);
        return modelAndView;
    }
}
