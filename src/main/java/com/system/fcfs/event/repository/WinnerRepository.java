package com.system.fcfs.event.repository;

import com.system.fcfs.event.domain.Winner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinnerRepository extends JpaRepository<Winner, Long> {
}
