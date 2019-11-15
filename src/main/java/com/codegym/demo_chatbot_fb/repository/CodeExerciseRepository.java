package com.codegym.demo_chatbot_fb.repository;

import com.codegym.demo_chatbot_fb.model.CodeExercise;
import com.codegym.demo_chatbot_fb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CodeExerciseRepository extends JpaRepository<CodeExercise, Long> {
    @Query("select c from CodeExercise c where c.status = true")
    Iterable<CodeExercise> findAllByStatusIsTrue();

    @Query("select c from CodeExercise c where c.status = false ")
    Iterable<CodeExercise> findAllByStatusIsFalse();

    Iterable<CodeExercise> findAllByTitleContaining(String s);
}
