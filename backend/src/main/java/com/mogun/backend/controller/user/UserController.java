package com.mogun.backend.controller.user;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.service.user.UserService;
import com.mogun.backend.service.user.dto.JoinDto;
import com.mogun.backend.service.user.request.UserJoinRequest;
import com.mogun.backend.service.user.response.IsJoinedResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/User")
public class UserController {

    private final UserService userService;

    @GetMapping("/isJoined")
    public ApiResponse<IsJoinedResponse> isJoined(@RequestParam String email) {
        boolean res = false;

        if(userService.isJoined(email) != 'N')
            res = true;


        return ApiResponse.ok(IsJoinedResponse.builder().isJoined(res).build());
    }

    @PostMapping("/Enroll")
    public ApiResponse enrollUser(@RequestBody UserJoinRequest userJoinRequest) {
        String result = userService.joinUser(JoinDto.builder()
                .email(userJoinRequest.getUserEmail())
                .password(userJoinRequest.getUserPassword())
                .name(userJoinRequest.getUserName())
                .gender(userJoinRequest.getGender().charAt(0))
                .height(userJoinRequest.getHeight())
                .weight(userJoinRequest.getWeight())
                .muscleMass(userJoinRequest.getMuscleMass())
                .bodyFat(userJoinRequest.getBodyFat())
                .build()
        );

        if(result == "SUCCESS")
            return ApiResponse.of(HttpStatus.ACCEPTED, "등록 완료", userJoinRequest);
        else
            return ApiResponse.badRequest(result);
    }
}
