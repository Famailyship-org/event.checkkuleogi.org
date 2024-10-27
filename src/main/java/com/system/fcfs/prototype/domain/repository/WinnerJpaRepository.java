package com.system.fcfs.prototype.domain.repository;

import com.system.fcfs.prototype.domain.Winner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface WinnerJpaRepository extends JpaRepository<Winner, Long> {

    @Query("SELECT w FROM Winner w ORDER BY w.time ASC")
    List<Winner> findTop100(Pageable pageable);
}
