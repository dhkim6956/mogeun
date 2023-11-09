package com.mogun.backend.domain.report.setReport.repository;

import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.setReport.SetReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SetReportRepository extends JpaRepository<SetReport, Integer> {

    @Query(value = "SELECT * FROM set_report sr WHERE sr.routine_report_key = :rrk ORDER BY start_time", nativeQuery = true)
    List<SetReport> findAllByRoutineReport(@Param("rrk") RoutineReport report);
}
