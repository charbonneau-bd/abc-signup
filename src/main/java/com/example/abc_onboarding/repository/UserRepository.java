package com.example.abc_onboarding.repository;

import com.example.abc_onboarding.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByAccountId(String accountId);
    boolean existsBySsn(String ssn);


}
