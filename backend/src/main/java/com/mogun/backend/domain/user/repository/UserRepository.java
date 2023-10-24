package com.mogun.backend.domain.user.repository;

import com.mogun.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);

    char findIsLeavedByUserKey(int userKey);
    boolean existsByEmail(String email);

}
