package com.mogun.backend.domain.report.routineResult.repository;

import com.mogun.backend.domain.report.routineResult.RoutineResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineResultRepository extends JpaRepository<RoutineResult, Integer> {
}
