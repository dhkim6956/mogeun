package com.mogun.backend.service.routine.userRoutinePlan;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.exercise.repository.ExerciseRepository;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutine.repository.UserRoutineRepository;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.routine.userRoutinePlan.repository.UserRoutinePlanRepository;
import com.mogun.backend.service.routine.dto.RoutineDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRoutinePlanService {

    private final UserRoutineRepository routineRepository;
    private final UserRoutinePlanRepository planRepository;
    private final ExerciseRepository exerciseRepository;

    public String addPlan(RoutineDto dto) {

        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());
        if(routine.isEmpty() || routine.get().getIsDeleted() == 'Y')
            return "요청 오류: 등록된 루틴이 아님";

        Optional<Exercise> exec = exerciseRepository.findById(dto.getExecKey());
        if(exec.isEmpty())
            return "요청 오류: 추가 가능한 운동이 없음";

        planRepository.save(dto.toRoutinePlanEntity(routine.get(), exec.get()));

        return "SUCCESS";
    }

    public List<RoutineDto> getAllPlan(RoutineDto dto) {

        List<RoutineDto> result = new ArrayList<>();
        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());
        List<UserRoutinePlan> planList = planRepository.findAllByUserRoutine(routine.get());

        for(UserRoutinePlan plan: planList) {
            result.add(RoutineDto.builder()
                    .planKey(plan.getRoutinePlanKey())
                    .exec(plan.getExercise())
                    .build());
        }

        return result;
    }
}
