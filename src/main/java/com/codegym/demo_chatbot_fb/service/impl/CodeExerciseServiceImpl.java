package com.codegym.demo_chatbot_fb.service.impl;

import com.codegym.demo_chatbot_fb.model.CodeExercise;
import com.codegym.demo_chatbot_fb.repository.CodeExerciseRepository;
import com.codegym.demo_chatbot_fb.service.CodeExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CodeExerciseServiceImpl implements CodeExerciseService {
    @Autowired
    private CodeExerciseRepository codeExerciseRepository;

    @Override
    public Iterable<CodeExercise> findAll() {
        return codeExerciseRepository.findAll();
    }

    @Override
    public void save(CodeExercise codeExercise) {
        codeExerciseRepository.save(codeExercise);
    }

    @Override
    public void delete(Long id) {
        Optional<CodeExercise> codeExercise = codeExerciseRepository.findById(id);
        if (codeExercise.isPresent()) codeExerciseRepository.delete(codeExercise.get());
    }

    @Override
    public Optional<CodeExercise> findById(Long id) {
        return codeExerciseRepository.findById(id);
    }

    @Override
    public Iterable<CodeExercise> findAllByStatusIsTrue() {
        return codeExerciseRepository.findAllByStatusIsTrue();
    }

    @Override
    public Iterable<CodeExercise> findAllByStatusIsFalse() {
        return codeExerciseRepository.findAllByStatusIsFalse();
    }

    @Override
    public Iterable<CodeExercise> findAllByTitleContaining(String s) {
        return codeExerciseRepository.findAllByTitleContaining(s);
    }

    @Override
    public CodeExercise findCodeExerciseTrueFirst() {
        ArrayList<CodeExercise> codeExercises = (ArrayList<CodeExercise>) codeExerciseRepository.findAllByStatusIsTrue();
        if (codeExercises.isEmpty()) return null;
        else return codeExercises.get(0);
    }
}
