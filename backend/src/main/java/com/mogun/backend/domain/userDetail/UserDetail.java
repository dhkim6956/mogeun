package com.mogun.backend.domain.userDetail;

import com.mogun.backend.domain.gender.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UserDetail {

    @Id
    private int detailId;

    @OneToOne
    @JoinColumn(name = "gender_code")
    private Gender gender;
    private int age;
    private float weight;
    private float height;
    private LocalDateTime createdDate;
}
