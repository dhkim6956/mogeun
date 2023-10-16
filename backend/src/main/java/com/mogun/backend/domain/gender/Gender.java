package com.mogun.backend.domain.gender;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Gender {

    @Id
    @Column(name = "gender_code")
    private int genderCode;
    private String genderName;
}
