package com.mogun.backend.domain.userLog.userMuscleMassLog.repository;

import com.mogun.backend.domain.userLog.userMuscleMassLog.UserMuscleMassLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMuscleMassLogRepository extends JpaRepository<UserMuscleMassLog, Long> {
}
