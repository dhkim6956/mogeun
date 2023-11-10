package com.mogun.backend.controller.exercise;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.exercise.request.ExerciseRequest;
import com.mogun.backend.controller.routine.response.PlanListResponse;
import com.mogun.backend.controller.routine.response.SimplePlanInfoResponse;
import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.service.ServiceStatus;
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
    public ApiResponse<Object> getAllExercise() {

        List<SimplePlanInfoResponse> data = new ArrayList<>();
        List<Exercise> exerciseList = exerciseService.getAllExercise().getData();

        for(Exercise item: exerciseList) {

            data.add(SimplePlanInfoResponse.builder()
                    .execKey(item.getExecKey())
                    .execName(item.getName())
                    .engName(item.getEngName())
                    .mainPart(item.getMainPart().getPartName())
                    .imagePath(item.getImagePath())
//                    .musclePart(attachPartService.getAllPartNameByExercise(item))
                    .build());
        }

        return ApiResponse.ok(data);
    }

    @GetMapping("/List")
    public ApiResponse<Object> getExercise(@RequestParam("exec_key") int execKey) {

        ServiceStatus<Exercise> result = exerciseService.getExercise(execKey);
        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        Exercise exec = result.getData();
        return ApiResponse.ok(SimplePlanInfoResponse.builder()
                .execKey(exec.getExecKey())
                .execName(exec.getName())
                .engName(exec.getEngName())
                .imagePath(exec.getImagePath())
                .musclePart(attachPartService.getAllPartNameByExercise(exec))
                .build());
    }

    @PostMapping("/Add")
    public ApiResponse<Object> createExercise(@RequestBody ExerciseRequest request) {

        ServiceStatus<Object> result = exerciseService.createExercise(ExerciseDto.builder()
                .execName(request.getExecName())
                .engName(request.getEngName())
                .imagePath(request.getImagePath())
                .partKey(request.getPartKey())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }
}
