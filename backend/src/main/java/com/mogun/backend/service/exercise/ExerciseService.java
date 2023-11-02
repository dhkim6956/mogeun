package com.mogun.backend.service.exercise;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.exercise.repository.ExerciseRepository;
import com.mogun.backend.service.exercise.dto.ExerciseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public List<Exercise> getAllExercise() {

        return exerciseRepository.findAll();
    }

    public Exercise getExercise(int execKey) {

        Optional<Exercise> exec = exerciseRepository.findById(execKey);
        if(exec.isEmpty()) return null;

        return exec.get();
    }

}
