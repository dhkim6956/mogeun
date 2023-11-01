package com.mogun.backend.domain.report.muscleActInSet.repository;

import com.mogun.backend.domain.report.muscleActInSet.MuscleActInSetLog;
import com.mogun.backend.domain.report.setReport.SetReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MuscleActInSetLogRepository extends JpaRepository<MuscleActInSetLog, Long> {

    List<MuscleActInSetLog> findAllBySetReport(SetReport report);
}
