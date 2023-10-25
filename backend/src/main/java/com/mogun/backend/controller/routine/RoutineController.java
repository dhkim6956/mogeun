package com.mogun.backend.controller.routine;


import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.controller.routine.response.SimpleRoutineInfoResponse;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.userRoutine.UserRoutineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Routine")
public class RoutineController {

    private final UserRoutineService routineService;

    @PostMapping("/Create")
    public ApiResponse createRoutine(@RequestBody CommonRoutineRequest request) {

        String result = routineService.createRoutine(RoutineDto.builder()
                .routineName(request.getRoutineName()).build(),
                request.getEmail());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @PutMapping("/Delete")
    public ApiResponse deleteRoutine(@RequestBody CommonRoutineRequest request) {

        String result = routineService.deleteRoutine(RoutineDto.builder()
                .routineKey(request.getRoutineKey())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @GetMapping("/ListAll")
    public ApiResponse getAllRoutine(@RequestParam String email) {

        List<SimpleRoutineInfoResponse> list = new ArrayList<>();
        List<RoutineDto> result = routineService.getAllRoutine(email);

        for(RoutineDto dto: result) {
            list.add(SimpleRoutineInfoResponse.builder()
                    .key(dto.getRoutineKey())
                    .name(dto.getRoutineName())
                    .build());
        }

        return ApiResponse.ok(list);
    }
}
