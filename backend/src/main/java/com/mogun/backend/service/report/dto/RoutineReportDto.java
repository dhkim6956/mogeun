package com.mogun.backend.service.report.dto;

import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RoutineReportDto {

    private String email;
    private User user;
    private int routineKey;
    private String routineName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private char isAttached;

    public RoutineReport toRoutineReportEntity() {

        return RoutineReport.builder()
                .user(user)
                .routineName(routineName)
                .startTime(startTime)
                .isAttached(isAttached)
                .build();
    }
}
