package com.mogun.backend.service.user;

import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.domain.userDetail.UserDetail;
import com.mogun.backend.domain.userDetail.repository.UserDetailRepository;
import com.mogun.backend.service.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    // GET Request 는 dto 객체로 전달
    // POST, PUT, PATCH, DELETE Request 는 Result String 으로 반환 -> 성공 시 SUCCESS, 이외엔 실패 사유

    public String joinUser(UserDto dto) {

        char joinState = isJoined(dto.getEmail());
        char userGender = dto.getGender();

        if(joinState == 'J')
            return "이미 등록된 이메일";
        if(joinState == 'E')
            return "탈퇴한 회원";

        if(!(userGender== 'm' || userGender == 'f'))
            return "지원 가능한 성별이 아님(m 혹은 f)";

        User savedUser = userRepository.save(dto.toEntity());
        UserDetail savedDetail = userDetailRepository.save(dto.toDetailedEntity(savedUser));

        return "SUCCESS";
    }

    public String exitUser(String email, String password) {

        char joinState = isJoined(email);
        if(joinState == 'E')
            return "요청 오류: 이미 탈퇴한 회원";

        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        if(user.isEmpty())
            return "요청 오류: 잘못된 회원 정보";

        user.get().setIsLeaved('E');

        return "SUCCESS";
    }

    public char isJoined(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent())
            return user.get().getIsLeaved();

        return 'N';
    }

    public UserDto getUserDetail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        Optional<UserDetail> userDetail = userDetailRepository.findById(user.get().getUserKey());
        return UserDto.builder()
                .height(userDetail.get().getHeight())
                .weight(userDetail.get().getWeight())
                .muscleMass(userDetail.get().getMuscleMass())
                .bodyFat(userDetail.get().getBodyFat())
                .build();
    }

    public String changePassword(String email, String oldPassword, String newPassword) {

        Optional<User> user = userRepository.findByEmailAndPassword(email, oldPassword);
        if(user.isEmpty())
            return "요청 오류: 잘못된 회원 정보";
        if(oldPassword.equals(newPassword))
            return "요청 오류: 동일한 비밀번호";

        user.get().setPassword(newPassword);
        return "SUCCESS";
    }
}
