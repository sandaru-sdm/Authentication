package com.sdm.Authentication.auth.repository;

import com.sdm.Authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByActivationCode(String activationCode);
    boolean existsByEmail(String email);
}
