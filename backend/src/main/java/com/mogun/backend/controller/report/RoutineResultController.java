package com.mogun.backend.controller.report;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.report.request.CommonResultRequest;
import com.mogun.backend.service.report.RoutineResultService;
import com.mogun.backend.service.report.dto.ResultDto;
import com.mogun.backend.service.report.dto.ResultListDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/Monthly")
    public ApiResponse getMonthlyResult(@RequestParam("user_key") int userKey) {

        List<ResultListDto> list =  resultService.getMonthlyResult(ResultDto.builder().userKey(userKey).build());
        if(list.get(0).getRoutineCount() == -1)
            return ApiResponse.badRequest("요청 오류: 등록된 회원이 아님");

        return  ApiResponse.ok(list);
    }
}
