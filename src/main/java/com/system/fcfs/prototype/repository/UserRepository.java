package com.system.fcfs.prototype.repository;

import com.system.fcfs.prototype.domain.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

    Optional<SiteUser> findByEmail(String email);

    SiteUser findById(String userId);
}
