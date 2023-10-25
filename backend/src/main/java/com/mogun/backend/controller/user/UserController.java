package com.mogun.backend.controller.user;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.user.request.ChangePasswordRequest;
import com.mogun.backend.controller.user.request.ExitRequest;
import com.mogun.backend.controller.user.request.SignInRequest;
import com.mogun.backend.controller.user.response.UserDetailResponse;
import com.mogun.backend.service.user.UserService;
import com.mogun.backend.service.user.dto.UserDto;
import com.mogun.backend.controller.user.request.UserJoinRequest;
import com.mogun.backend.controller.user.response.IsJoinedResponse;
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
        boolean res = true;

        char joinState = userService.isJoined(email);
        if(joinState == 'J')
            return ApiResponse.ok(IsJoinedResponse.builder()
                    .isJoined(true)
                    .joinState("가입된 회원")
                    .build());
        else if(joinState == 'E')
            return ApiResponse.ok(IsJoinedResponse.builder()
                    .isJoined(false)
                    .joinState("탈퇴한 회원")
                    .build());
        else
            return ApiResponse.ok(IsJoinedResponse.builder()
                    .isJoined(false)
                    .joinState("가입하지 않은 회원")
                    .build());
    }

    @PostMapping("/Enroll")
    public ApiResponse enrollUser(@RequestBody UserJoinRequest userJoinRequest) {
        String result = userService.joinUser(UserDto.builder()
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

    @PostMapping("/Exit")
    public ApiResponse exitUser(@RequestBody ExitRequest exitRequest) {

        String result = userService.exitUser(exitRequest.getUserId(), exitRequest.getUserPassword());

        if(result != "SUCCESS")
            return ApiResponse.badRequest(result);
        else
            return ApiResponse.of(HttpStatus.ACCEPTED, result, null);
    }

    @PostMapping("/Change/Password")
    public ApiResponse changePassword(@RequestBody ChangePasswordRequest pwdReq) {

        String result = userService.changePassword(pwdReq.getEmail(), pwdReq.getOldPassword(), pwdReq.getNewPassword());
        if(result != "SUCCESS")
            return ApiResponse.badRequest(result);
        else
            return ApiResponse.of(HttpStatus.ACCEPTED, result, null);
    }

    @GetMapping("/Detail")
    public ApiResponse getUserDetail(@RequestParam String email) {

        UserDto result = userService.getUserDetail(email);

        return ApiResponse.ok(UserDetailResponse.builder()
                .height(result.getHeight())
                .weight(result.getWeight())
                .muscleMass(result.getMuscleMass())
                .bodyFat(result.getBodyFat())
                .build());
    }

    @PostMapping("/SignIn")
    public ApiResponse signIn(@RequestBody SignInRequest request) {

        String result = userService.signIn(request.getUserEmail(), request.getUserPassword());

        if(result != "SUCCESS")
            return ApiResponse.badRequest(result);

        return ApiResponse.of(HttpStatus.ACCEPTED, result, null);
    }
}
