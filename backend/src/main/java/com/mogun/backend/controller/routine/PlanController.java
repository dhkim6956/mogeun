package com.mogun.backend.controller.routine;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.controller.routine.response.PlanListResponse;
import com.mogun.backend.controller.routine.response.SimplePlanInfoResponse;
import com.mogun.backend.service.attachPart.AttachPartService;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.userRoutine.UserRoutineService;
import com.mogun.backend.service.routine.userRoutinePlan.UserRoutinePlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Routine/Plan")
public class PlanController {

    private final AttachPartService attachPartService;
    private final UserRoutineService routineService;
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

    @GetMapping("/ListAll")
    public ApiResponse getAllPlan(@RequestParam("routine_key") int routineKey) {

        RoutineDto dto = routineService.getRoutine(routineKey);
        List<SimplePlanInfoResponse> planList = new ArrayList<>();

        List<RoutineDto> list = planService.getAllPlan(RoutineDto.builder()
                .routineKey(routineKey)
                .build());

        for(RoutineDto item: list) {

            List<String> parts = attachPartService.getAllPartNameByExercise(item.getExec());

            planList.add(SimplePlanInfoResponse.builder()
                    .execKey(item.getExec().getExecKey())
                    .execName(item.getExec().getName())
                    .musclePart(parts)
                    .build());
        }

        return ApiResponse.ok(PlanListResponse.builder()
                .routineKey(dto.getRoutineKey())
                .routineName(dto.getRoutineName())
                .exercises(planList)
                .build());
    }

}
