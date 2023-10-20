package com.mogun.backend.domain.userLog.userWeightLog;

import com.mogun.backend.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserWeightLog {

    @Id
    @Column(name = "log_key")
    private Long logKey;

    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    @Column(name = "weight_before")
    private float weightBefore;

    @Column(name = "weight_after")
    private float weightAfter;

    @Column(name = "changed_time")
    private LocalDateTime changedTime;
}
