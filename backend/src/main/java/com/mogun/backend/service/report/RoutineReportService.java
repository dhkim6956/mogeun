package com.mogun.backend.service.report;

import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.routineReport.repository.RoutineReportRepository;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutine.repository.UserRoutineRepository;
import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.service.report.dto.RoutineReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoutineReportService {

    private final UserRepository userRepository;
    private final UserRoutineRepository routineRepository;
    private final RoutineReportRepository routineReportRepository;

    public String startRoutineReport(RoutineReportDto dto) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty())
            return "요청 오류: 등록된 회원이 아님";

        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());
        if(routine.isEmpty() || routine.get().getIsDeleted() == 'Y')
            return "요청 오류: 등록된 루틴이 아님";

        if(!routine.get().getUser().equals(user.get()))
            return "요청 오류: 현재 회원이 소유한 루틴이 아님";

        dto.setStartTime(LocalDateTime.now());

        routineReportRepository.save(dto.toRoutineReportEntity(user.get(), routine.get()));

        return "SUCCESS";
    }

    public String endRoutineReport(RoutineReportDto dto) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty())
            return "요청 오류: 등록된 회원이 아님";

        Optional<RoutineReport> report = routineReportRepository.findById(dto.getReportKey());
        if(report.isEmpty())
            return "요청 오류: 기록된 루틴 로그가 없음";

        if(report.get().getEndTime() != null)
            return "요청 오류: 해당 로그는 이미 종료됨";

        report.get().setEndTime(LocalDateTime.now());

        return "SUCCESS";
    }
}
