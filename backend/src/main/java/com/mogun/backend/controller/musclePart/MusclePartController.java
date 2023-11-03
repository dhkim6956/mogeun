package com.mogun.backend.controller.musclePart;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.musclePart.request.MusclePartRequest;
import com.mogun.backend.service.attachPart.MusclePartService;
import com.mogun.backend.service.attachPart.dto.MusclePartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Muscle")
public class MusclePartController {

    private final MusclePartService musclePartService;

    @PostMapping("/Insert")
    public ApiResponse insertMusclePart(@RequestBody MusclePartRequest request) {

        String result = musclePartService.insertMusclePart(MusclePartDto.builder()
                .partName(request.getPartName())
                .imagePath(request.getImagePath())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }
}
