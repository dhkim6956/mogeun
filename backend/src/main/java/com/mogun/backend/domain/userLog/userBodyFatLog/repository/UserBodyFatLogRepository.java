package com.mogun.backend.domain.userLog.userBodyFatLog.repository;

import com.mogun.backend.domain.userLog.userBodyFatLog.UserBodyFatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBodyFatLogRepository extends JpaRepository<UserBodyFatLog, Long> {
}
