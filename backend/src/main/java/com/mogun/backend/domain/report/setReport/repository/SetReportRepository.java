package com.mogun.backend.domain.report.setReport.repository;

import com.mogun.backend.domain.report.setReport.SetReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetReportRepository extends JpaRepository<SetReport, Integer> {
}
