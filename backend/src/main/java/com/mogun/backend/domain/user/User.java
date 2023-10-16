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
    private int userId;

    @OneToOne
    @JoinColumn(name = "detailId")
    private UserDetail userDetail;

    private String email;
    private String password;
    private String nickname;
}
