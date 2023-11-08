package com.mogun.backend.domain.attachPart.repository;

import com.mogun.backend.domain.attachPart.AttachPart;
import com.mogun.backend.domain.exercise.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachPartRepository extends JpaRepository<AttachPart, Integer> {

    List<AttachPart> findAllByExercise(Exercise exercise);

    // Seongmin 부착 부위 왼쪽만 가져오기
    List<AttachPart> findAllByExerciseAndAttachDirection(Exercise exercise, char L);
}
