package com.mogun.backend.controller.routine;


import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.controller.routine.request.SetInfo;
import com.mogun.backend.controller.routine.request.SetRequest;
import com.mogun.backend.controller.routine.request.SetRequestList;
import com.mogun.backend.controller.routine.response.AllSetInfoResponse;
import com.mogun.backend.domain.routine.setDetail.SetDetail;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.setDetail.SetDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Routine/Set")
public class SetDetailController {

    private final SetDetailService setDetailService;

    @PostMapping("/Add")
    public ApiResponse<Object> addOneSet(@RequestBody SetRequest request) {

        ServiceStatus<Object> result = setDetailService.addOneSetGoal(RoutineDto.builder()
                .planKey(request.getPlanKey())
                .setNumber(request.getSetNumber())
                .weight(request.getWeight())
                .targetRep(request.getTargetRep())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @PostMapping("/AddAll")
    public ApiResponse<Object> addAllSet(@RequestBody SetRequestList requestList) {

        List<RoutineDto> dtoList = new ArrayList<>();

        for(SetInfo info: requestList.getSetInfoList()) {

            dtoList.add(RoutineDto.builder()
                    .planKey(requestList.getPlan_key())
                    .setNumber(info.getSetNumber())
                    .weight(info.getWeight())
                    .targetRep(info.getTargetRep())
                    .build());
        }

        ServiceStatus<Object> result = setDetailService.addAllSetGoal(dtoList);

        return ApiResponse.postAndPutResponse(result, requestList);
    }

    @RequestMapping("/ListAll")
    public ApiResponse<Object> getAll(@RequestParam("plan_key") int planKey) {

        List<SetInfo> infoList = new ArrayList<>();
        List<SetDetail> setDetailList = setDetailService.getAllSetInfo(RoutineDto.builder()
                .planKey(planKey)
                .build());

        if(setDetailList.get(0).getSetKey() == -1)
            return ApiResponse.badRequest("요청 오류: 등록된 적 없는 운동 계획");

        String execName = setDetailList.get(0).getUserRoutinePlan().getExercise().getName();

        for(SetDetail item: setDetailList) {
            infoList.add(SetInfo.builder()
                    .setNumber(item.getSetNumber())
                    .weight(item.getWeight())
                    .targetRep(item.getTargetRepeat())
                    .build());
        }

        return ApiResponse.ok(AllSetInfoResponse.builder()
                .execName(execName)
                .setAmount(setDetailList.size())
                .setInfoList(infoList)
                .build());
    }

    @DeleteMapping("/Delete")
    public ApiResponse<Object> deleteOneSet(@RequestBody CommonRoutineRequest request) {

        ServiceStatus<Object> result = setDetailService.deleteOneSet(RoutineDto.builder().setKey(request.getSetKey()).build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @DeleteMapping("/DeleteAll")
    public ApiResponse<Object> deleteAll(@RequestBody CommonRoutineRequest request) {

        ServiceStatus<Object> result = setDetailService.deleteAllSet(RoutineDto.builder().planKey(request.getPlanKey()).build());

        return ApiResponse.postAndPutResponse(result, request);
    }
}
