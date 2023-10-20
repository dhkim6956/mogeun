package com.mogun.backend.domain.userLog.userWeightLog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserWeightLog {

    @Id
    @Column(name = "log_key")
    private Long logKey;


}
