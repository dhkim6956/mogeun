package com.mogun.backend.service.routine.userRoutine;

import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutine.repository.UserRoutineRepository;
import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
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
public class UserRoutineService {

    private final UserRepository userRepository;
    private final UserRoutineRepository routineRepository;

    public RoutineDto getRoutine(int routineKey) {

        Optional<UserRoutine> routine = routineRepository.findById(routineKey);
        if(routine.isEmpty())
            return null;

        return RoutineDto.builder()
                .routineKey(routine.get().getRoutineKey())
                .routineName(routine.get().getRoutineName())
                .build();
    }

    public int createRoutine(RoutineDto dto, int userKey) {

        Optional<User> user = userRepository.findById(userKey);
        if(user.isEmpty() || user.get().getIsLeaved() != 'J')
            return -1;

        UserRoutine save = routineRepository.save(dto.toRoutineEntity(user.get()));

        return save.getRoutineKey();
    }

    public String renameRoutine(RoutineDto dto) {

        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());

        if(routine.isEmpty() || routine.get().getIsDeleted() == 'Y')
            return "요청 오류: 등록된 루틴이 아님";

        routine.get().setRoutineName(dto.getRoutineName());
        return "SUCCESS";
    }

    public String deleteRoutine(RoutineDto dto) {

        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());

        if(routine.isEmpty() || routine.get().getIsDeleted() == 'Y')
            return "요청 오류: 등록된 루틴이 아님";

        routine.get().setIsDeleted('Y');
        return "SUCCESS";
    }

    public List<RoutineDto> getAllRoutine(int userKey) {

        List<RoutineDto> dtoList = new ArrayList<>();
        Optional<User> user = userRepository.findById(userKey);
        if(user.isEmpty() || user.get().getIsLeaved() != 'J')
            return dtoList;

        List<UserRoutine> result = routineRepository.findAllByUser(user.get());
        for(UserRoutine routine: result) {

            if(routine.getIsDeleted() == 'N')
                dtoList.add(RoutineDto.builder()
                        .routineKey(routine.getRoutineKey())
                        .routineName(routine.getRoutineName())
                        .build());
        }

        return dtoList;
    }
}
