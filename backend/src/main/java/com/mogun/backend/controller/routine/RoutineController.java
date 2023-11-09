package com.mogun.backend.controller.routine;


import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.controller.routine.response.RoutineCreatedResponse;
import com.mogun.backend.controller.routine.response.SimpleRoutineInfoResponse;
import com.mogun.backend.domain.routine.userRoutinePlan.repository.UserRoutinePlanRepository;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Routine")
public class RoutineController {

    private final UserRoutineService routineService;
    private final UserRoutinePlanService planService;

    @PostMapping("/Create")
    public ApiResponse createRoutine(@RequestBody CommonRoutineRequest request) {

        int result = routineService.createRoutine(RoutineDto.builder()
                .routineName(request.getRoutineName()).build(),
                request.getUserKey());

        if(result == -1)
            return ApiResponse.badRequest("요청 오류: 등록되지 않은 회원");

        return ApiResponse.of(HttpStatus.ACCEPTED, "SUCCESS", RoutineCreatedResponse.builder()
                .routine_key(result)
                .routineName(request.getRoutineName())
                .build());
    }

    @PutMapping("/Delete")
    public ApiResponse deleteRoutine(@RequestBody CommonRoutineRequest request) {

        String result = routineService.deleteRoutine(RoutineDto.builder()
                .routineKey(request.getRoutineKey())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @PutMapping("/Rename")
    public ApiResponse renameRoutine(@RequestBody CommonRoutineRequest request) {

        String result = routineService.renameRoutine(RoutineDto.builder()
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
    public ApiResponse getAllRoutine(@RequestParam("user_key") int userKey) {

        return ApiResponse.ok(planService.getAllRoutineAndMuscle(RoutineDto.builder().userKey(userKey).build()));
    }
}
