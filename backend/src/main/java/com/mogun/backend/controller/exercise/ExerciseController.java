package com.mogun.backend.controller.exercise;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.response.PlanListResponse;
import com.mogun.backend.controller.routine.response.SimplePlanInfoResponse;
import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.service.attachPart.AttachPartService;
import com.mogun.backend.service.exercise.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final AttachPartService attachPartService;

    @GetMapping("ListAll")
    public ApiResponse getAllExercise() {

        List<SimplePlanInfoResponse> result = new ArrayList<>();
        List<Exercise> exerciseList = exerciseService.getAllExercise();

        for(Exercise item: exerciseList) {

            result.add(SimplePlanInfoResponse.builder()
                    .execKey(item.getExecKey())
                    .execName(item.getName())
                    .engName(item.getEngName())
                    .musclePart(attachPartService.getAllPartNameByExercise(item))
                    .build());
        }

        return ApiResponse.ok(result);
    }
}
