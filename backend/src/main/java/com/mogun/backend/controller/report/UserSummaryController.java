package com.mogun.backend.controller.report;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.domain.report.setReport.SetReport;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.report.SetReportService;
import com.mogun.backend.service.report.dto.MostPerformedDto;
import com.mogun.backend.service.report.dto.MostSetsDto;
import com.mogun.backend.service.report.dto.MostWeightDto;
import com.mogun.backend.service.report.dto.RoutineReportDto;
import com.mogun.backend.service.userLog.UserLogService;
import com.mogun.backend.service.userLog.dto.UserLogDto;
import com.mogun.backend.service.userLog.dto.UserMuscleAndFatLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Summary")
public class UserSummaryController {

    private final UserLogService logService;
    private final SetReportService setReportService;

    @GetMapping("/LastLogs")
    public ApiResponse<Object> getLast10BodyFatAndMuscleMassLogs(@RequestParam("user_key") int userKey) {

        ServiceStatus<UserMuscleAndFatLogDto> result = logService.getLast10Logs(UserLogDto.builder()
                .userKey(userKey)
                .build());

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }

    @GetMapping("/ExerciseMost")
    public ApiResponse<Object> getMostExercised(@RequestParam("user_key")int userKey, @RequestParam("search_type")int option) {

        ServiceStatus<MostPerformedDto> result = setReportService.mostPerformedExercise(RoutineReportDto.builder()
                .userKey(userKey).build(), option);

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }

    @GetMapping("/ExerciseWeight")
    public ApiResponse<Object> getMostWeighted(@RequestParam("user_key")int userKey, @RequestParam("search_type")int option) {

        ServiceStatus<MostWeightDto> result = setReportService.mostWeightedExercise(RoutineReportDto.builder()
                .userKey(userKey).build(), option);

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }

    @GetMapping("/ExerciseSet")
    public ApiResponse<Object> getMostSet(@RequestParam("user_key")int userKey, @RequestParam("search_type")int option) {

        ServiceStatus<MostSetsDto> result = setReportService.mostSetExercise(RoutineReportDto.builder()
                .userKey(userKey).build(), option);

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }
}
