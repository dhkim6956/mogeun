package com.mogun.backend.service.user;

import com.mogun.backend.controller.ApiResponse;
import com.mogun.backend.domain.user.Repository.UserRepository;
import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.userDetail.Repository.UserDetailRepository;
import com.mogun.backend.domain.userDetail.UserDetail;
import com.mogun.backend.service.user.Dto.JoinUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    public ApiResponse insertUser(JoinUserDto dto) {
        boolean flag = userRepository.existsByEmail(dto.getEmail());
        if(flag) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, "중복된 이메일", dto.getEmail());
        }

        UserDetail detail = UserDetail.builder()
                        .gender(dto.getGender())
                        .age(dto.getAge())
                        .height(dto.getHeight())
                        .weight(dto.getWeight())
                        .build();

        User user = User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .userDetail(detail)
                .build();

        userRepository.save(user);

        return ApiResponse.of(HttpStatus.ACCEPTED, "OK", user);
    }
}
