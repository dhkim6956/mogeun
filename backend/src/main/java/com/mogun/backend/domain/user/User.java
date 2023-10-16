package com.mogun.backend.domain.user;

import com.mogun.backend.domain.userDetail.UserDetail;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 값을 null로 하면 DB에서 AUTO_INCREMENT
    @Column(name = "user_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "detail_id")
    private UserDetail userDetail;

    private String email;
    private String password;
    private String nickname;

    @Builder
    public User (UserDetail userDetail, String email, String password, String nickname) {
        this.userDetail = userDetail;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
