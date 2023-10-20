package com.mogun.backend.domain.routine.userRoutine;

import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoutine {

    @Id
    @Column(name = "routine_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int routineKey;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    @Column(name = "routine_name")
    private String routineName;

    @Column
    private int count;

    @Column(name = "is_deleted")
    private char isDeleted;
}
