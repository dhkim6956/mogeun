package com.mogun.backend.service.report;

import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.routineReport.repository.RoutineReportRepository;
import com.mogun.backend.domain.report.setReport.repository.SetReportRepository;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.routine.userRoutinePlan.repository.UserRoutinePlanRepository;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.report.dto.RoutineReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SetReportService {

    private final SetReportRepository setReportRepository;
    private final RoutineReportRepository routineReportRepository;
    private final UserRoutinePlanRepository planRepository;

    public ServiceStatus insertSetReport(RoutineReportDto dto) {

        Optional<RoutineReport> report = routineReportRepository.findById(dto.getReportKey());
        if(report.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 루틴 로그가 없음");

        Optional<UserRoutinePlan> plan = planRepository.findById(dto.getPlanKey());
        if(plan.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 회원의 루틴 혹은 운동 계획이 없음");

        if(!report.get().getUser().equals(plan.get().getUser()))
            return ServiceStatus.errorStatus("요청 오류: 로그 요청 회원과 루틴 소유 회원 불일치");

        setReportRepository.save(dto.toSetReportEntity(report.get(), plan.get().getExercise()));

        return ServiceStatus.okStatus();
    }
}
