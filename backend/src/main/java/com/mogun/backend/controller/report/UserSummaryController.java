package com.mogun.backend.controller.report;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.domain.report.setReport.SetReport;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.report.SetReportService;
import com.mogun.backend.service.userLog.UserLogService;
import com.mogun.backend.service.userLog.dto.UserLogDto;
import com.mogun.backend.service.userLog.dto.UserMuscleAndFatLogDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Summary")
public class UserSummaryController {

    private UserLogService logService;
    private SetReportService setReportService;

    @GetMapping("/LastLogs")
    public ApiResponse<Object> getLast10BodyFatAndMuscleMassLogs(@RequestParam("user_key") int userKey) {

        ServiceStatus<UserMuscleAndFatLogDto> result = logService.getLast10Logs(UserLogDto.builder()
                .userKey(userKey)
                .build());

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }


}
