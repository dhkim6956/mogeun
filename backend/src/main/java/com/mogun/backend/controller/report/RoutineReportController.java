package com.mogun.backend.controller.report;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.report.request.CommonReportRequest;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.report.RoutineReportService;
import com.mogun.backend.service.report.SetReportService;
import com.mogun.backend.service.report.dto.RoutineReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Report/Routine")
public class RoutineReportController {

    private final RoutineReportService routineReportService;
    private final SetReportService setReportService;

    @PostMapping("/Start")
    public ApiResponse<Object> startRoutineReport(@RequestBody CommonReportRequest request) {

        ServiceStatus<Long> result = routineReportService.startRoutineReport(RoutineReportDto.builder()
                .userKey(request.getUserKey())
                .routineKey(request.getRoutineKey())
                .isAttached(request.getIsAttached())
                .build());
        request.setReportKey(result.getData());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @PutMapping("/End")
    public ApiResponse<Object> endRoutineReport(@RequestBody CommonReportRequest request) {

        ServiceStatus<Object> result = routineReportService.endRoutineReport(RoutineReportDto.builder()
                .reportKey(request.getRoutineReportKey())
                .userKey(request.getUserKey())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @PostMapping("/Set")
    public ApiResponse<Object> insertSetReport(@RequestBody CommonReportRequest request) {

        ServiceStatus<Object> result = setReportService.insertSetReport(RoutineReportDto.builder()
                .reportKey(request.getRoutineReportKey())
                .planKey(request.getPlanKey())
                .setNumber(request.getSetNumber())
                .muscleAverage(request.getMuscleAverage())
                .muscleFatigue(request.getMuscleFatigue())
                .weight(request.getWeight())
                .targetRep(request.getTargetRepeat())
                .successRep(request.getSuccessRepeat())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

}