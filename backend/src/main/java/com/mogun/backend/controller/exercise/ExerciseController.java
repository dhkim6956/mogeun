package com.mogun.backend.controller.exercise;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.exercise.request.ExerciseRequest;
import com.mogun.backend.controller.routine.response.PlanListResponse;
import com.mogun.backend.controller.routine.response.SimplePlanInfoResponse;
import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.service.attachPart.AttachPartService;
import com.mogun.backend.service.exercise.ExerciseService;
import com.mogun.backend.service.exercise.dto.ExerciseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final AttachPartService attachPartService;

    @GetMapping("/ListAll")
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

    @GetMapping("/List")
    public ApiResponse getExercise(@RequestParam("exec_key") int execKey) {

        Exercise exec = exerciseService.getExercise(execKey);
        if(exec == null)
            return ApiResponse.badRequest("요청 오류: 등록되지 않은 운동 목록");

        return ApiResponse.ok(SimplePlanInfoResponse.builder()
                .execKey(exec.getExecKey())
                .execName(exec.getName())
                .engName(exec.getEngName())
                .musclePart(attachPartService.getAllPartNameByExercise(exec))
                .build());
    }

    @PostMapping("/Add")
    public ApiResponse createExercise(@RequestBody ExerciseRequest request) {

        String result = exerciseService.createExercise(ExerciseDto.builder()
                .execName(request.getExecName())
                .engName(request.getEngName())
                .imagePath(request.getImagePath())
                .partKey(request.getPartKey())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }
}
