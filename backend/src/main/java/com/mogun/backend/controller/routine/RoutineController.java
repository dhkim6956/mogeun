package com.mogun.backend.controller.routine;


import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.controller.routine.response.SimpleRoutineInfoResponse;
import com.mogun.backend.service.attachPart.AttachPartService;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.userRoutine.UserRoutineService;
import com.mogun.backend.service.routine.userRoutinePlan.UserRoutinePlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    // Seongmin
    private final UserRoutinePlanService planService;
    private final AttachPartService attachPartService;

    @PostMapping("/Create")
    public ApiResponse createRoutine(@RequestBody CommonRoutineRequest request) {

        String result = routineService.createRoutine(RoutineDto.builder()
                .routineName(request.getRoutineName()).build(),
                request.getUserKey());

        return ApiResponse.postAndPutResponse(result, request);
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

        List<SimpleRoutineInfoResponse> list = new ArrayList<>();
        List<String> partsList = new ArrayList<>();
        List<RoutineDto> result = routineService.getAllRoutine(userKey);

        for(RoutineDto dto: result) {
            List<RoutineDto> plan = planService.getAllPlan(RoutineDto.builder()
                    .routineKey(dto.getRoutineKey())
                    .build());

            for(RoutineDto item: plan) {
                List<String> parts = attachPartService.getPartImagePathByExercise(item.getExec());

                partsList.addAll(parts);
            }

            List<String> partsImagePath = partsList.stream().distinct().collect(Collectors.toList());
            partsList.clear();

            list.add(SimpleRoutineInfoResponse.builder()
                    .key(dto.getRoutineKey())
                    .name(dto.getRoutineName())
                    .imagePath(partsImagePath)
                    .build());
        }

        return ApiResponse.ok(list);
    }
}
