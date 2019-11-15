package com.codegym.demo_chatbot_fb.service;

import com.codegym.demo_chatbot_fb.model.CodeExercise;
import org.springframework.data.domain.Page;

public interface CodeExerciseService extends GeneralService<CodeExercise, Long> {
    Iterable<CodeExercise> findAllByTitleContaining(String s);

    CodeExercise findCodeExerciseTrueFirst();
}
