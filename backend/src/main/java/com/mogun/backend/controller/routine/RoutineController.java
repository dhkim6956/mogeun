package com.mogun.backend.controller.routine;


import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.controller.routine.response.RoutineCreatedResponse;
import com.mogun.backend.controller.routine.response.SimpleRoutineInfoResponse;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutinePlan.repository.UserRoutinePlanRepository;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.attachPart.AttachPartService;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.dto.RoutineOutlineDto;
import com.mogun.backend.service.routine.userRoutine.UserRoutineService;
import com.mogun.backend.service.routine.userRoutinePlan.UserRoutinePlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Routine")
public class RoutineController {

    private final UserRoutineService routineService;
    private final UserRoutinePlanService planService;

    @PostMapping("/Create")
    public ApiResponse<Object> createRoutine(@RequestBody CommonRoutineRequest request) {

        ServiceStatus<UserRoutine> result = routineService.createRoutine(RoutineDto.builder()
                .routineName(request.getRoutineName()).build(),
                request.getUserKey());

        if(result.getStatus() != 100)
            return ApiResponse.badRequest("요청 오류: 등록되지 않은 회원");

        UserRoutine routine = result.getData();

        return ApiResponse.of(HttpStatus.ACCEPTED, "SUCCESS", RoutineCreatedResponse.builder()
                .routine_key(routine.getRoutineKey())
                .routineName(routine.getRoutineName())
                .build());
    }

    @PutMapping("/Delete")
    public ApiResponse<Object> deleteRoutine(@RequestBody CommonRoutineRequest request) {

        ServiceStatus<Object> result = routineService.deleteRoutine(RoutineDto.builder()
                .routineKey(request.getRoutineKey())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @PutMapping("/Rename")
    public ApiResponse<Object> renameRoutine(@RequestBody CommonRoutineRequest request) {

        ServiceStatus<Object> result = routineService.renameRoutine(RoutineDto.builder()
                .routineKey(request.getRoutineKey())
                .routineName(request.getRoutineName())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

//    @GetMapping("/ListAll")
//    public ApiResponse getAllRoutine(@RequestParam("user_key") int userKey) {
//
//        List<SimpleRoutineInfoResponse> list = new ArrayList<>();
//        List<RoutineDto> result = routineService.getAllRoutine(userKey);
//
//        for(RoutineDto dto: result) {
//            list.add(SimpleRoutineInfoResponse.builder()
//                    .key(dto.getRoutineKey())
//                    .name(dto.getRoutineName())
//                    .build());
//        }
//
//        return ApiResponse.ok(list);
//    }

    // Seongmin 루틴에서 사용하는 근육 이미지 포함
    @GetMapping("/ListAll")
    public ApiResponse<Object> getAllRoutine(@RequestParam("user_key") int userKey) {

        ServiceStatus<List<RoutineOutlineDto>> result = planService.getAllRoutineAndMuscle(RoutineDto.builder().userKey(userKey).build());
        if(result.getStatus() == 200)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }
}
