package com.mogun.backend.controller.user;

import com.mogun.backend.controller.ApiResponse;
import com.mogun.backend.controller.user.request.UserRequest;
import com.mogun.backend.service.user.Dto.JoinUserDto;
import com.mogun.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<JoinUserDto> insertUser(@RequestBody UserRequest request) {
        JoinUserDto dto = request.toDto();
        System.out.println(dto);
        return userService.insertUser(dto);
    }
}
