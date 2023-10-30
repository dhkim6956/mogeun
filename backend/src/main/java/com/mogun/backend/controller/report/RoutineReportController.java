package com.mogun.backend.controller.report;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.report.request.CommonReportRequest;
import com.mogun.backend.service.report.RoutineReportService;
import com.mogun.backend.service.report.dto.RoutineReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Report/Routine")
public class RoutineReportController {

    private final RoutineReportService routineReportService;

    @PostMapping("/Start")
    public ApiResponse insertRoutineReport(@RequestBody CommonReportRequest request) {

        String result = routineReportService.insertRoutineReport(RoutineReportDto.builder()
                .email(request.getEmail())
                .routineKey(request.getRoutineKey())
                .isAttached(request.getIsAttached())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }
}
