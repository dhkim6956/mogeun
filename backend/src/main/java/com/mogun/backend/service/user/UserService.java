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
    // POST, PUT, PATCH, DELETE Request 는 Result String 으로 반환 -> 성공 시 SUCCESS, 이외엔 실패 사유

    public String joinUser(JoinDto dto) {

        if(isJoined(dto.getEmail()) == 'J')
            return "이미 등록된 이메일";
        if(isJoined(dto.getEmail()) == 'E')
            return "탈퇴한 회원";

        if(!(dto.getGender() == 'm' || dto.getGender() == 'f'))
            return "지원 가능한 성별이 아님(m 혹은 f)";

        User savedUser = userRepository.save(dto.toEntity());
        System.out.println("userKey: " + savedUser.getUserKey());
        UserDetail savedDetail = userDetailRepository.save(dto.toDetailedEntity(savedUser));

        return "SUCCESS";
    }

    public char isJoined(String email) {
        User user = null;

        boolean flag = userRepository.existsByEmail(email);
        if(flag) {
            user = userRepository.findByEmail(email);

            return user.getIsLeaved();
        }

        return 'N';
    }
}
