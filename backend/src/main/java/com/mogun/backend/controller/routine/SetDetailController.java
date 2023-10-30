package com.mogun.backend.controller.routine;


import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.controller.routine.request.SetInfo;
import com.mogun.backend.controller.routine.request.SetRequest;
import com.mogun.backend.controller.routine.request.SetRequestList;
import com.mogun.backend.controller.routine.response.AllSetInfoResponse;
import com.mogun.backend.domain.routine.setDetail.SetDetail;
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
    public ApiResponse addOneSet(@RequestBody SetRequest request) {

        String result = setDetailService.addOneSetGoal(RoutineDto.builder()
                .planKey(request.getPlanKey())
                .setNumber(request.getSetNumber())
                .weight(request.getWeight())
                .targetRep(request.getTargetRep())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @PostMapping("/AddAll")
    public ApiResponse addAllSet(@RequestBody SetRequestList requestList) {

        List<RoutineDto> dtoList = new ArrayList<>();

        for(SetInfo info: requestList.getSetInfoList()) {

            dtoList.add(RoutineDto.builder()
                    .planKey(requestList.getPlan_key())
                    .setNumber(info.getSetNumber())
                    .weight(info.getWeight())
                    .targetRep(info.getTargetRep())
                    .build());
        }

        String result = setDetailService.addAllSetGoal(dtoList);

        return ApiResponse.postAndPutResponse(result, requestList);
    }

    @RequestMapping("/ListAll")
    public ApiResponse getAll(@RequestParam("plan_key") int planKey) {

        List<SetInfo> infoList = new ArrayList<>();
        List<SetDetail> setDetailList = setDetailService.getAllSetInfo(RoutineDto.builder()
                .planKey(planKey)
                .build());

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
    public ApiResponse deleteOneSet(@RequestBody CommonRoutineRequest request) {

        String result = setDetailService.deleteOneSet(RoutineDto.builder().setKey(request.getSetKey()).build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @DeleteMapping("/DeleteAll")
    public ApiResponse deleteAll(@RequestBody CommonRoutineRequest request) {

        String result = setDetailService.deleteAllSet(RoutineDto.builder().planKey(request.getPlanKey()).build());

        return ApiResponse.postAndPutResponse(result, request);
    }
}
