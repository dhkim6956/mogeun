package com.mogun.backend.domain.routine.setDetail;

import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SetDetail implements Serializable {

    @Id
    @Column(name = "user_key")
    private int userKey;

    @Id
    @Column(name = "routine_key")
    private int routineKey;

    @Id
    @Column(name = "routine_plan_key")
    private int routinePlanKey;

    @Column(name = "set_number")
    private int setNumber;

    @Column
    private int weight;

    @Column
    private int target_repeat;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "routine_key")
    private UserRoutine userRoutine;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "routine_plan_key")
    private UserRoutinePlan userRoutinePlan;
}
