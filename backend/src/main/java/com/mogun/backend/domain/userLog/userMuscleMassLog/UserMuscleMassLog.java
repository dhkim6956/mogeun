package com.mogun.backend.domain.userLog.userMuscleMassLog;

import com.mogun.backend.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserMuscleMassLog {

    @Id
    @Column(name = "log_key")
    private Long logKey;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    @Column(name = "muscle_mass_before")
    private float muscleMassBefore;

    @Column(name = "muscle_mass_after")
    private float muscleMassAfter;

    @Column(name = "changed_time")
    private LocalDateTime changedTime;
}
