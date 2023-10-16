package com.mogun.backend.domain.userDetail;

import com.mogun.backend.domain.gender.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetail {

    @Id
    @Column(name = "detail_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int gender;
    private int age;
    private float weight;
    private float height;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder
    public UserDetail(int gender, int age, float weight, float height) {
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.createdDate = LocalDateTime.now();
    }
}
