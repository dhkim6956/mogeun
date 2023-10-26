package com.mogun.backend.controller.routine;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.userRoutinePlan.UserRoutinePlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Routine/Plan")
public class PlanController {

    private final UserRoutinePlanService planService;

    @PostMapping("/Add")
    public ApiResponse addPlan(@RequestBody CommonRoutineRequest request) {

        String result = planService.addPlan(RoutineDto.builder()
                .routineKey(request.getRoutineKey())
                .execKey(request.getExecKey())
                .setAmount(request.getSets())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

}
