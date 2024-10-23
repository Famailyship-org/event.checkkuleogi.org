package com.system.fcfs.prototype.repository;

import com.system.fcfs.prototype.domain.SiteUser;
import com.system.fcfs.prototype.domain.WinnerLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WinnerLogRepository extends JpaRepository<WinnerLog, Long> {

    Optional<WinnerLog> findByWinner(SiteUser user);
}
