package com.athdhup.sewnso.repository;

import com.athdhup.sewnso.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // Used constantly for login/auth
    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}