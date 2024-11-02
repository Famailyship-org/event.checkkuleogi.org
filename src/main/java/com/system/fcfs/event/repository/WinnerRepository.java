package com.system.fcfs.event.repository;

import com.system.fcfs.event.domain.Winner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WinnerRepository extends JpaRepository<Winner, Long> {
    Optional<Winner> findByUserNameAndPhoneNum(String userName, String phoneNum);
}
