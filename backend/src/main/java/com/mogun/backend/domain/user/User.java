package com.mogun.backend.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userKey;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private char gender;

    @Column(name = "is_leaved")
    private char isLeaved;

    @Column(name = "join_time")
    private LocalDateTime joinTime;

    @Builder
    public User(String email, String password, String name, char gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.isLeaved = 'J';
        this.joinTime = LocalDateTime.now();
    }
}
