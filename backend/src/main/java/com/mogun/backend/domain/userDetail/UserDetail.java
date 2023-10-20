package com.mogun.backend.domain.userDetail;

import com.mogun.backend.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class UserDetail {

    @Id
    @Column(name = "user_key")
    private int userKey;

    @Column
    private float weight;

    @Column
    private float height;

    @Column(name = "muscle_mass")
    private float muscleMass;

    @Column(name = "body_fat")
    private float bodyFat;

    @OneToOne
    @MapsId // @Id로 지정한 컬럼을 @OneToOne 혹은 @ManyToOne 관계를 매핑
    @JoinColumn(name = "user_key")
    private User user;

}
