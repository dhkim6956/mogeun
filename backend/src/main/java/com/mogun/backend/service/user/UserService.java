package com.mogun.backend.service.user;

import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.domain.userDetail.UserDetail;
import com.mogun.backend.domain.userDetail.repository.UserDetailRepository;
import com.mogun.backend.service.user.dto.JoinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    // GET Request 는 Dto 객체로 전달
    // POST, PUT, PATCH, DELETE Request 는 Result String 으로 반환

    public String joinUser(JoinDto dto) {
        User user = null;

        // Id 중복 검사
        boolean flag = userRepository.existsByEmail(dto.getEmail());
        if(flag) {
            user = userRepository.findByEmail(dto.getEmail());
        }
        if(flag && user.getIsLeaved() == 'J')
            return "이미 등록된 회원 이메일";
        if(flag && user.getIsLeaved() == 'E')
            return "탈퇴한 회원";

        if(dto.getGender() != 'm' || dto.getGender() != 'f')
            return "지원 가능한 성별이 아님";

        User savedUser = userRepository.save(dto.toEntity());
        UserDetail savedDetail = userDetailRepository.save(dto.toDetailedEntity(savedUser));

        return "SUCCESS";
    }
}
