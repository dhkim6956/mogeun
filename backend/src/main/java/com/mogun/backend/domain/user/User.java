package com.mogun.backend.domain.user;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void setIsLeaved(char state) {
        this.isLeaved = state;
    }
}
