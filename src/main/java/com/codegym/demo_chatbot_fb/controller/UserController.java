package com.codegym.demo_chatbot_fb.controller;

import com.codegym.demo_chatbot_fb.model.User;
import com.codegym.demo_chatbot_fb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ModelAndView listUsers(){
        ModelAndView modelAndView = new ModelAndView("user/list");
        Iterable<User> users = userService.findAll();
        modelAndView.addObject("users",users);
        modelAndView.addObject("usersTrue", userService.findAllByStatusIsTrue());
        modelAndView.addObject("usersFalse", userService.findAllByStatusIsFalse());
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
        modelAndView.addObject("message","Delete done");
        userService.delete(id);
        return modelAndView;
    }
}
