package com.mogun.backend.controller.report;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.report.request.CommonReportRequest;
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
    public ApiResponse startRoutineReport(@RequestBody CommonReportRequest request) {

        String result = routineReportService.startRoutineReport(RoutineReportDto.builder()
                .email(request.getEmail())
                .routineKey(request.getRoutineKey())
                .isAttached(request.getIsAttached())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @PutMapping("/End")
    public ApiResponse endRoutineReport(@RequestBody CommonReportRequest request) {

        String result = routineReportService.endRoutineReport(RoutineReportDto.builder()
                .reportKey(request.getRoutineReportKey())
                .email(request.getEmail())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @PostMapping("/Set")
    public ApiResponse insertSetReport(@RequestBody CommonReportRequest request) {

        String result = setReportService.insertSetReport(RoutineReportDto.builder()
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
