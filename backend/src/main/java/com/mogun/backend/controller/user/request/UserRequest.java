package com.mogun.backend.controller.user.request;

import com.mogun.backend.service.user.Dto.JoinUserDto;
import lombok.Builder;
import lombok.Data;

@Data
public class UserRequest {

    private String email;
    private String password;
    private String nickname;

    private int gender;
    private int age;
    private float weight;
    private float height;

    @Builder
    private UserRequest(String email, String password, String nickname, int gender, int age, float weight, float height) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }

    public JoinUserDto toDto() {
        return JoinUserDto.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .age(this.age)
                .weight(this.weight)
                .height(this.height)
                .build();
    }
}
