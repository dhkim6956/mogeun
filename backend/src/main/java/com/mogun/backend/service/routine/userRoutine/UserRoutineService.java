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

    public String createRoutine(RoutineDto dto, String email) {

        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty() || user.get().getIsLeaved() != 'J')
            return "요청 오류: 등록된 회원이 아님";

        routineRepository.save(dto.toRoutineEntity(user.get()));

        return "SUCCESS";
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

    public List<RoutineDto> getAllRoutine(String email) {

        List<RoutineDto> dtoList = new ArrayList<>();
        Optional<User> user = userRepository.findByEmail(email);
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
