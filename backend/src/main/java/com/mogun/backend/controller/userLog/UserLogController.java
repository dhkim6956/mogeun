package com.mogun.backend.controller.userLog;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.userLog.request.CommonChangeRequest;
import com.mogun.backend.service.userLog.UserLogService;
import com.mogun.backend.service.userLog.dto.UserLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/User/Log")
public class UserLogController {

    private final UserLogService logService;

    @PostMapping("/Change/Height")
    public ApiResponse changeHeight(@RequestBody CommonChangeRequest request) {

        String result = logService.changeHeight(UserLogDto.builder()
                .userKey(request.getUserKey())
                .heightAfter(request.getHeight())
                .build());

        if(result != "SUCCESS")
            return ApiResponse.badRequest(result);

        return ApiResponse.of(HttpStatus.ACCEPTED, result, null);
    }

    @PostMapping("/Change/Weight")
    public ApiResponse changeWeight(@RequestBody CommonChangeRequest request) {

        String result = logService.changeWeight(UserLogDto.builder()
                .userKey(request.getUserKey())
                .weightAfter(request.getWeight())
                .build());

        if(result != "SUCCESS")
            return ApiResponse.badRequest(result);

        return ApiResponse.of(HttpStatus.ACCEPTED, result, null);
    }

    @PostMapping("/Change/MuscleMass")
    public ApiResponse changeMuscleMass(@RequestBody CommonChangeRequest request) {

        String result = logService.changeMuscleMass(UserLogDto.builder()
                .userKey(request.getUserKey())
                .muscleMassAfter(request.getMuscleMass())
                .build());

        if(result != "SUCCESS")
            return ApiResponse.badRequest(result);

        return ApiResponse.of(HttpStatus.ACCEPTED, result, null);
    }

    @PostMapping("/Change/BodyFat")
    public ApiResponse changeBodyFat(@RequestBody CommonChangeRequest request) {

        String result = logService.changeBodyFat(UserLogDto.builder()
                .userKey(request.getUserKey())
                .bodyFatAfter(request.getBodyFat())
                .build());

        if(result != "SUCCESS")
            return ApiResponse.badRequest(result);

        return ApiResponse.of(HttpStatus.ACCEPTED, result, null);
    }

    // Seongmin Change/All API 추가
    @PutMapping("/Change/All")
    public ApiResponse changeAll(@RequestBody CommonChangeRequest request) {

        String result = logService.changeAll(UserLogDto.builder()
                .userKey(request.getUserKey())
                .userName(request.getUserName())
                .heightAfter(request.getHeight())
                .weightAfter(request.getWeight())
                .muscleMassAfter(request.getMuscleMass())
                .bodyFatAfter(request.getBodyFat())
                .build());

        if(result != "SUCCESS")
            return ApiResponse.badRequest(result);

        return ApiResponse.of(HttpStatus.ACCEPTED, result, null);
    }
}
