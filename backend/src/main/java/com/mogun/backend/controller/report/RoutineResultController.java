package com.mogun.backend.controller.report;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.report.request.CommonResultRequest;
import com.mogun.backend.service.report.RoutineResultService;
import com.mogun.backend.service.report.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Result")
public class RoutineResultController {

    private final RoutineResultService resultService;

    @PostMapping("/Create")
    public ApiResponse createResult(@RequestBody CommonResultRequest request) {

        String result = resultService.createResult(ResultDto.builder()
                .reportKey(request.getReportKey())
                .consumeCalorie(request.getConsumeCalorie())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }
}
