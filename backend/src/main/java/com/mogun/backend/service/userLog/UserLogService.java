package com.mogun.backend.service.userLog;

import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.domain.userDetail.UserDetail;
import com.mogun.backend.domain.userDetail.repository.UserDetailRepository;
import com.mogun.backend.domain.userLog.userBodyFatLog.repository.UserBodyFatLogRepository;
import com.mogun.backend.domain.userLog.userHeightLog.repository.UserHeightLogRepository;
import com.mogun.backend.domain.userLog.userMuscleMassLog.repository.UserMuscleMassLogRepository;
import com.mogun.backend.domain.userLog.userWeightLog.repository.UserWeightLogRepository;

import com.mogun.backend.service.userLog.dto.UserLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLogService {

    private final UserRepository userRepository;
    private final UserDetailRepository detailRepository;
    private final UserHeightLogRepository heightLogRepository;
    private final UserWeightLogRepository weightLogRepository;
    private final UserMuscleMassLogRepository muscleMassLogRepository;
    private final UserBodyFatLogRepository bodyFatLogRepository;


    public String changeHeight(UserLogDto dto) {

        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if(user.isEmpty() || user.get().getIsLeaved() == 'E')
            return "요청 오류: 등록된 회원이 아님";

        Optional<UserDetail> detail = detailRepository.findById(user.get().getUserKey());
        dto.setHeightBefore(detail.get().getHeight());
        heightLogRepository.save(dto.toHeightLogEntity(user.get()));
        detail.get().setHeight(dto.getHeightAfter());

        return "SUCCESS";
    }

    public String changeWeight(UserLogDto dto) {

        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if(user.isEmpty() || user.get().getIsLeaved() == 'E')
            return "요청 오류: 등록된 회원이 아님";

        Optional<UserDetail> detail = detailRepository.findById(user.get().getUserKey());
        dto.setWeightBefore(detail.get().getWeight());
        weightLogRepository.save(dto.toWeightLogEntity(user.get()));
        detail.get().setWeight(dto.getWeightAfter());

        return "SUCCESS";
    }

    public String changeMuscleMass(UserLogDto dto) {

        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if(user.isEmpty() || user.get().getIsLeaved() == 'E')
            return "요청 오류: 등록된 회원이 아님";

        Optional<UserDetail> detail = detailRepository.findById(user.get().getUserKey());
        dto.setMuscleMassBefore(detail.get().getMuscleMass());
        muscleMassLogRepository.save(dto.toMuscleMassLogEntity(user.get()));
        detail.get().setMuscleMass(dto.getMuscleMassAfter());

        return "SUCCESS";
    }

    public String changeBodyFat(UserLogDto dto) {

        Optional<User> user = userRepository.findByEmail(dto.getEmail());
        if(user.isEmpty() || user.get().getIsLeaved() == 'E')
            return "요청 오류: 등록된 회원이 아님";

        Optional<UserDetail> detail = detailRepository.findById(user.get().getUserKey());
        dto.setBodyFatBefore(detail.get().getBodyFat());
        bodyFatLogRepository.save(dto.toBodyFatLogEntity(user.get()));
        detail.get().setBodyFat(dto.getBodyFatAfter());

        return "SUCCESS";
    }
}
