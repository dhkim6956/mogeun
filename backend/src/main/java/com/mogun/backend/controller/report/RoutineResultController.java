package com.mogun.backend.controller.report;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.report.request.CommonResultRequest;
import com.mogun.backend.controller.report.response.ExerciseResultResponse;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.report.RoutineResultService;
import com.mogun.backend.service.report.dto.ResultDto;
import com.mogun.backend.service.report.dto.ResultListDto;
import com.mogun.backend.service.report.dto.SetResultListDto;
import com.mogun.backend.service.report.dto.SummaryResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Result")
public class RoutineResultController {

    private final RoutineResultService resultService;

    @PostMapping("/Create")
    public ApiResponse<Object> createResult(@RequestBody CommonResultRequest request) {

        ServiceStatus<Object> result = resultService.createResult(ResultDto.builder()
                .reportKey(request.getReportKey())
                .consumeCalorie(request.getConsumeCalorie())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @GetMapping("/Routine")
    public ApiResponse<Object> getRoutineResult(@RequestParam("user_key") int userKey, @RequestParam("routine_result_key") int resultKey) {

        ServiceStatus<Object> result = resultService.getAllInfoOfResult(ResultDto.builder()
                .userKey(userKey)
                .resultKey(resultKey)
                .build());

        if(result.getStatus() == 300)
            return ApiResponse.badRequest("요청 오류: 해당 루틴 기록이 없음");
        else if(result.getStatus() == 200)
            return ApiResponse.badRequest("요청 오류: 등록된 회원이 아님");
        else if(result.getStatus() == 400)
            return ApiResponse.badRequest("요청 오류: 루틴 기록을 소유한 회원과 요청 회원이 불일치");

        return ApiResponse.ok(result.getData());
    }

    @GetMapping("/Exercise")
    public ApiResponse<Object> getExerciseResult(@RequestParam("user_key") int userKey, @RequestParam("routine_result_key") int resultKey) {

        List<SetResultListDto> list = resultService.getExerciseResult(ResultDto.builder()
                .userKey(userKey)
                .resultKey(resultKey)
                .build());

        if(list.get(0).getStatus() == -1)
            return ApiResponse.badRequest("요청 오류: 등록된 회원이 아님");
        else if(list.get(0).getStatus() == -2)
            return ApiResponse.badRequest("요청 오류: 해당 루틴 기록이 없음");
        else if(list.get(0).getStatus() == -3)
            return ApiResponse.badRequest("요청 오류: 루틴 기록을 소유한 회원과 요청 회원이 불일치");


        List<ExerciseResultResponse> responses = new ArrayList<>();
        for(SetResultListDto item: list) {
            responses.add(ExerciseResultResponse.builder()
                    .execName(item.getExec().getName())
                    .imagePath(item.getExec().getImagePath())
                    .setResultList(item.getSetResultDtoList())
                    .build());
        }

        return  ApiResponse.ok(responses);
    }

    @GetMapping("/LastMonth")
    public ApiResponse<Object> getLastMonthResult(@RequestParam("user_key") int userKey) {

        List<ResultListDto> list =  resultService.getLastMonthResult(ResultDto.builder().userKey(userKey).build());
        if(!list.isEmpty() && list.get(0).getRoutineCount() == -1)
            return ApiResponse.badRequest("요청 오류: 등록된 회원이 아님");

        return  ApiResponse.ok(list);
    }

    @GetMapping("/Monthly")
    public ApiResponse<Object> getMonthlyResult(@RequestParam("user_key") int userKey, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        List<ResultListDto> list = resultService.getMonthlyRangeResult(ResultDto.builder()
                .userKey(userKey)
                .date(date)
                .build());

        if(!list.isEmpty() && list.get(0).getRoutineCount() == -1)
            return ApiResponse.badRequest("요청 오류: 등록된 회원이 아님");

        return  ApiResponse.ok(list);
    }
}
