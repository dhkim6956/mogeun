package com.mogun.backend.domain.gender;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gender {

    @Id
    @Column(name = "gender_code")
    private int id;
    private String genderName;
}
