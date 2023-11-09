package com.mogun.backend.service.routine.userRoutinePlan;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.exercise.repository.ExerciseRepository;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutine.repository.UserRoutineRepository;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.routine.userRoutinePlan.repository.UserRoutinePlanRepository;
import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.dto.RoutineOutlineDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRoutinePlanService {

    private final UserRoutineRepository routineRepository;
    private final UserRoutinePlanRepository planRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

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

    public String removePlan(RoutineDto dto) {

        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());
        Optional<Exercise> exercise = exerciseRepository.findById(dto.getExecKey());

        if(routine.isEmpty())
            return  "요청 오류: 등록된 루틴이 아님";
        if(exercise.isEmpty())
            return "요청 오류: 목록에 없는 운동";

        Optional<UserRoutinePlan> plan = planRepository.findByUserRoutineAndExercise(routine.get(), exercise.get());
        if(plan.isEmpty())
            return "요청 오류: 적합한 운동 계획이 없음";
        planRepository.delete(plan.get());

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

    public List<RoutineOutlineDto> getAllRoutineAndMuscle(RoutineDto dto) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty())
            return null;

        List<UserRoutinePlan> planList = planRepository.findAllByUser(user.get());
        List<RoutineOutlineDto> routineOutlineDtoList = new ArrayList<>();

        for(UserRoutinePlan plan: planList) {
            if(routineOutlineDtoList.isEmpty()) {
                routineOutlineDtoList.add(RoutineOutlineDto.builder()
                        .name(plan.getUserRoutine().getRoutineName())
                        .routineKey(plan.getUserRoutine().getRoutineKey())
                        .muscleImagePathList(new HashSet<>())
                        .build());
            }

            int lastIndex = routineOutlineDtoList.size() - 1;
            UserRoutine routine = plan.getUserRoutine();
            Exercise exec = plan.getExercise();

            if(routineOutlineDtoList.get(lastIndex).getRoutineKey() == routine.getRoutineKey()) {
                routineOutlineDtoList.get(lastIndex).getMuscleImagePathList().add(exec.getMainPart().getImagePath());
            } else {
                routineOutlineDtoList.add(RoutineOutlineDto.builder()
                        .name(plan.getUserRoutine().getRoutineName())
                        .routineKey(plan.getUserRoutine().getRoutineKey())
                        .muscleImagePathList(new HashSet<>())
                        .build());

                lastIndex = routineOutlineDtoList.size() - 1;
                routineOutlineDtoList.get(lastIndex).getMuscleImagePathList().add(exec.getMainPart().getImagePath());
            }

        }

        return routineOutlineDtoList;
    }
}
