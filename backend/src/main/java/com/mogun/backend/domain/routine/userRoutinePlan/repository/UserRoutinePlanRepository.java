package com.mogun.backend.domain.routine.userRoutinePlan.repository;

import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoutinePlanRepository extends JpaRepository<UserRoutinePlan, Integer> {
}
