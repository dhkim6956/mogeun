package com.mogun.backend.service.report;

import com.mogun.backend.domain.musclePart.MusclePart;
import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.routineReport.repository.RoutineReportRepository;
import com.mogun.backend.domain.report.routineResult.RoutineResult;
import com.mogun.backend.domain.report.routineResult.repository.RoutineResultRepository;
import com.mogun.backend.domain.report.usedMusclePart.repository.UsedMusclePartRepository;
import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.service.report.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoutineResultService {

    private final RoutineReportRepository reportRepository;
    private final RoutineResultRepository resultRepository;
    private final UsedMusclePartRepository usedMusclePartRepository;

    public String createResult(ResultDto dto) {

        Optional<RoutineReport> report = reportRepository.findById(dto.getReportKey());
        if(report.isEmpty())
            return "요청 오류: 등록된 적 없는 루틴 기록";

        Optional<RoutineResult> result = resultRepository.findByRoutineReport(report.get());
        if(result.isPresent())
            return "요청 오류: 기록된 루틴 결과에 대한 재작성";

        resultRepository.save(dto.toRoutineResultEntity(report.get()));


        return "SUCCESS";
    }
}
