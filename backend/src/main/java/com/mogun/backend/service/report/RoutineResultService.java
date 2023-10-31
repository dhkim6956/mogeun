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
import com.mogun.backend.service.report.dto.ResultListDto;
import com.mogun.backend.service.report.dto.SimpleReportInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoutineResultService {

    private final UserRepository userRepository;
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

    public List<ResultListDto> getMonthlyResult(ResultDto dto) {

        List<ResultListDto> list = new ArrayList<>();
        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty()) {
            list.add(ResultListDto.builder().routineCount(-1).build());
            return list;
        }

        LocalDate lastDate = LocalDate.now().minusMonths(1);


        List<RoutineResult> results = resultRepository.findAllByFromRoutineDateAndUser(lastDate, user.get());

        for(RoutineResult result: results) {
            if(list.isEmpty()) {
                list.add(ResultListDto.builder()
                        .date(result.getRoutineDate())
                        .routineCount(1)
                        .routineReports(new ArrayList<>())
                        .build());
            }

            int lastIndex = list.size() - 1;
            RoutineReport report = result.getRoutineReport();

            if(list.get(lastIndex).getDate().isEqual(result.getRoutineDate())) {
                list.get(lastIndex).getRoutineReports().add(SimpleReportInfo.builder()
                        .routineName(report.getRoutineName())
                        .reportKey(report.getRoutineReportKey())
                        .startTime(report.getStartTime())
                        .endTime(report.getEndTime())
                        .build());
            } else {
                list.add(ResultListDto.builder()
                        .date(result.getRoutineDate())
                        .routineCount(1)
                        .routineReports(new ArrayList<>())
                        .build());
                lastIndex = list.size() - 1;
                list.get(lastIndex).getRoutineReports().add(SimpleReportInfo.builder()
                        .routineName(report.getRoutineName())
                        .reportKey(report.getRoutineReportKey())
                        .startTime(report.getStartTime())
                        .endTime(report.getEndTime())
                        .build());
            }
        }

        return list;
    }
}
