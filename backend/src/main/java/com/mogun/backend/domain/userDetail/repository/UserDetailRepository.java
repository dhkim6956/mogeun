package com.mogun.backend.domain.userDetail.repository;

import com.mogun.backend.domain.userDetail.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Integer> {
}
