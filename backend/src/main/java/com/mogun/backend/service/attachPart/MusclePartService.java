package com.mogun.backend.service.attachPart;

import com.mogun.backend.domain.musclePart.repository.MusclePartRepository;
import com.mogun.backend.service.attachPart.dto.MusclePartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MusclePartService {

    private final MusclePartRepository musclePartRepository;

    public String insertMusclePart(MusclePartDto dto) {

        musclePartRepository.save(dto.toMusclePartEntity());

        return "SUCCESS";
    }
}
