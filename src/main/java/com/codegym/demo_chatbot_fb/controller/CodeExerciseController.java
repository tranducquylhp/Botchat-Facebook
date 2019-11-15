package com.codegym.demo_chatbot_fb.controller;

import com.codegym.demo_chatbot_fb.model.CodeExercise;
import com.codegym.demo_chatbot_fb.service.CodeExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class CodeExerciseController {
    @Autowired
    private CodeExerciseService codeExerciseService;

    @GetMapping("/codeExercises")
    public ModelAndView codeList(@RequestParam("s") Optional<String> s, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("codeExercise/list");
        if (s.isPresent()){
            modelAndView.addObject("codeExercises", codeExerciseService.findAllByTitleContaining(s.get()));
        } else modelAndView.addObject("codeExercises", codeExerciseService.findAll());
        if (request.getParameter("message")!= null){
            modelAndView.addObject("message",request.getParameter("message"));
        }
        modelAndView.addObject("codeExercisesTrue", codeExerciseService.findAllByStatusIsTrue());
        modelAndView.addObject("codeExercisesFalse", codeExerciseService.findAllByStatusIsFalse());

        return modelAndView;
    }

    @GetMapping("/codeExercise/create")
    public ModelAndView createForm(){
        ModelAndView modelAndView = new ModelAndView("codeExercise/create");
        modelAndView.addObject("codeExercise", new CodeExercise());

        return modelAndView;
    }

    @PostMapping("/codeExercise/create")
    public ModelAndView create(@ModelAttribute CodeExercise codeExercise){
        ModelAndView modelAndView = new ModelAndView("redirect:/codeExercises");
        modelAndView.addObject("message", "Create done");
        codeExerciseService.save(codeExercise);

        return modelAndView;
    }

    @GetMapping("/codeExercise/edit/{id}")
    public ModelAndView editForm(@PathVariable Long id){
        ModelAndView modelAndView;
        if (codeExerciseService.findById(id).isPresent()) {
            modelAndView = new ModelAndView("codeExercise/edit");
            modelAndView.addObject("codeExercise", codeExerciseService.findById(id).get());
        } else modelAndView = new ModelAndView("error");

        return modelAndView;
    }

    @PostMapping("/codeExercise/edit/{id}")
    public ModelAndView edit(@ModelAttribute CodeExercise codeExercise){
        ModelAndView modelAndView = new ModelAndView("redirect:/codeExercises");
        modelAndView.addObject("message", "Edit done");
        codeExerciseService.save(codeExercise);

        return modelAndView;
    }

    @GetMapping("/codeExercise/delete/{id}")
    public ModelAndView deleteForm(@PathVariable Long id){
        ModelAndView modelAndView;
        if (codeExerciseService.findById(id).isPresent()) {
            modelAndView = new ModelAndView("codeExercise/delete");
            modelAndView.addObject("codeExercise", codeExerciseService.findById(id).get());
        } else modelAndView = new ModelAndView("error");

        return modelAndView;
    }

    @PostMapping("/codeExercise/delete/{id}")
    public ModelAndView delete(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("redirect:/codeExercises");
        modelAndView.addObject("message", "Delete done");
        codeExerciseService.delete(id);

        return modelAndView;
    }

    @GetMapping("/codeExercise/view/{id}")
    public ModelAndView view(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("codeExercise/view");
        if (codeExerciseService.findById(id).isPresent()){
            modelAndView.addObject(codeExerciseService.findById(id).get());
        } else modelAndView.addObject(codeExerciseService.findById(id).get());

        return modelAndView;
    }
}
