package com.mogun.backend.domain.routine.userRoutinePlan.repository;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoutinePlanRepository extends JpaRepository<UserRoutinePlan, Integer> {

    List<UserRoutinePlan> findAllByUserRoutine(UserRoutine routine);
    Optional<UserRoutinePlan> findByUserRoutineAndExercise(UserRoutine routine, Exercise exec);
}
