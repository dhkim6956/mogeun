package com.mogun.backend.service.exercise;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.exercise.repository.ExerciseRepository;
import com.mogun.backend.domain.musclePart.MusclePart;
import com.mogun.backend.domain.musclePart.repository.MusclePartRepository;
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
    private final MusclePartRepository musclePartRepository;

    public List<Exercise> getAllExercise() {

        return exerciseRepository.findAll();
    }

    public Exercise getExercise(int execKey) {

        Optional<Exercise> exec = exerciseRepository.findById(execKey);
        if(exec.isEmpty()) return null;

        return exec.get();
    }

    public String createExercise(ExerciseDto dto) {

        Optional<MusclePart> musclePart = musclePartRepository.findById(dto.getPartKey());
        if(musclePart.isEmpty())
            return "요청 오류: DB에 등록되지 않은 근육 부위";

        dto.setPart(musclePart.get());
        exerciseRepository.save(dto.toExerciseEntity());

        return "SUCCESS";
    }
}
