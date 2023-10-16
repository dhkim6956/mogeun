package com.mogun.backend.domain.user.Repository;

import com.mogun.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // JpaRepository를 상속했다면 해당 Annotation 생략 가능
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);
    User findByEmail(String email);
}
