package com.mogun.backend.service.attachPart;

import com.mogun.backend.domain.attachPart.AttachPart;
import com.mogun.backend.domain.attachPart.repository.AttachPartRepository;
import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AttachPartService {

    private final ExerciseRepository exerciseRepository;
    private final AttachPartRepository attachPartRepository;

    public List<String> getAllPartNameByExercise(Exercise exec) {

        List<String> result = new ArrayList<>();
        List<AttachPart> partList = attachPartRepository.findAllByExercise(exec);

        for(AttachPart item: partList) {

            if(item.getAttachDirection() == 'L')
                result.add("왼쪽 " + item.getMusclePart().getPartName());
            else
                result.add("오른쪽 " + item.getMusclePart().getPartName());
        }

        return result;
    }
}
