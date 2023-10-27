package com.mogun.backend.service.exercise.dto;


import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.musclePart.MusclePart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ExerciseDto {

    private int execKey;
    private String execName;
    private String description;
    private String imagePath;
    private MusclePart part;

    public Exercise toExerciseEntity() {

        return Exercise.builder()
                .name(execName)
                .execDesc(description)
                .imagePath(imagePath)
                .mainPart(part)
                .build();
    }
}
