package com.mogun.backend.service.user.Dto;

import com.mogun.backend.domain.gender.Gender;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JoinUserDto {

    // standard user info
    private String email;
    private String password;
    private String nickname;

    // detailed user info
    private int gender;
    private int age;
    private float weight;
    private float height;

    @Builder
    public JoinUserDto(String email, String password, String nickname, int gender, int age, float weight, float height) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
    }
}
