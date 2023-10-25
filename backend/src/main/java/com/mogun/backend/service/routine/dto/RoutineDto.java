package com.mogun.backend.service.routine.dto;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.routine.setDetail.SetDetail;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoutineDto {

    // 루틴 정보
    private int routineKey;
    private String routineName;
    private int performCount;
    private char isDelete;

    // 단일 운동 계획 정보
    private int planKey;
    private UserRoutine routine;
    private Exercise exec;
    private int setAmount;

    // 계획 내 세트별 세부 사항
    private UserRoutinePlan plan;
    private int setNumber;
    private int weight;
    private int targetRep;

    public UserRoutine toRoutineEntity(User user) {
        return UserRoutine.builder()
                .user(user)
                .routineName(routineName)
                .count(0)
                .isDeleted('N')
                .build();
    }

    public UserRoutinePlan toRoutinePlanEntity(UserRoutine routine) {
        return UserRoutinePlan.builder()
                .userRoutine(routine)
                .user(routine.getUser())
                .exercise(exec)
                .setAmount(setAmount)
                .build();
    }

    public SetDetail toSetDetailEntity(UserRoutinePlan routinePlan) {
        return SetDetail.builder()
                .userRoutinePlan(routinePlan)
                .routinePlanKey(routinePlan.getRoutinePlanKey())
                .userRoutine(routinePlan.getUserRoutine())
                .routineKey(routinePlan.getUserRoutine().getRoutineKey())
                .user(routinePlan.getUser())
                .userKey(routinePlan.getUser().getUserKey())
                .setNumber(setNumber)
                .weight(weight)
                .targetRepeat(targetRep)
                .build();
    }
}
