package com.mogun.backend.domain.gender.Repository;

import com.mogun.backend.domain.gender.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender, Integer> {

    String findGenderNameById(int id);
}
