package com.mogun.backend.controller.routine;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.AddPlanListRequest;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.controller.routine.response.AddPlanListResponse;
import com.mogun.backend.controller.routine.response.PlanListResponse;
import com.mogun.backend.controller.routine.response.SimplePlanInfoResponse;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.attachPart.AttachPartService;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.userRoutine.UserRoutineService;
import com.mogun.backend.service.routine.userRoutinePlan.UserRoutinePlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

        ServiceStatus result = planService.addPlan(RoutineDto.builder()
                .routineKey(request.getRoutineKey())
                .execKey(request.getExecKey())
                .setAmount(request.getSets())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @PostMapping("/AddAll")
    public ApiResponse addAllPlan(@RequestBody AddPlanListRequest request) {

        List<Integer> success = new ArrayList<>();
        List<Integer> fail = new ArrayList<>();

        for(Integer key: request.getExecKeys()) {
            ServiceStatus result = planService.addPlan(RoutineDto.builder()
                    .routineKey(request.getRoutineKey())
                    .execKey(key)
                    .setAmount(1)
                    .build());

            if(result.getStatus() != 100) {
                fail.add(key);
            } else {
                success.add(key);
            }
        }

        return ApiResponse.of(HttpStatus.ACCEPTED, "OK", AddPlanListResponse.builder().addedExec(success).failedExec(fail).build());
    }

    @DeleteMapping("/Remove")
    public ApiResponse removePlan(@RequestParam("routine_key") int routineKey, @RequestParam("exec_key") int execKey) {

        ServiceStatus result = planService.removePlan(RoutineDto.builder()
                .routineKey(routineKey)
                .execKey(execKey)
                .build());

        return ApiResponse.postAndPutResponse(result, null);
    }

    @GetMapping("/ListAll")
    public ApiResponse getAllPlan(@RequestParam("routine_key") int routineKey) {

        ServiceStatus routineResult = routineService.getRoutine(routineKey);
        if(routineResult.getStatus() == 200)
            return ApiResponse.badRequest(routineResult.getMessage());

        RoutineDto dto = (RoutineDto) routineResult.getData();
        List<SimplePlanInfoResponse> planList = new ArrayList<>();

        ServiceStatus planResult = planService.getAllPlan(RoutineDto.builder()
                .routineKey(routineKey)
                .build());


//        for(RoutineDto item: list) {

//            List<String> parts = attachPartService.getAllPartNameByExercise(item.getExec());
            // Seongmin 사용 근육 가져오기
//            List<String> parts = attachPartService.getPartNameByExercise(item.getExec());
//
//            planList.add(SimplePlanInfoResponse.builder()
//                    .execKey(item.getExec().getExecKey())
//                    .execName(item.getExec().getName())
//                    .imagePath(item.getExec().getImagePath())
//                    .musclePart(parts)
//                    .engName(item.getExec().getEngName())
//                    .mainPart(item.getExec().getMainPart().getPartName())
//                    .build());
//        }
//
//        return ApiResponse.ok(PlanListResponse.builder()
//                .routineKey(dto.getRoutineKey())
//                .routineName(dto.getRoutineName())
//                .exercises(planList)
//                .build());

        return  ApiResponse.ok(planResult.getData());
    }

}
