package com.system.fcfs.event.repository;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.Event;
import com.system.fcfs.event.domain.Winner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface AttemptJpaRepository extends JpaRepository<Attempt, Long> {

    @Query("SELECT a FROM Attempt a ORDER BY a.timeStamp ASC")
    Optional<List<Winner>> findTop100(Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Attempt a WHERE a.userName = :userId")
    boolean existsByUserId(@Param("userId") String userId);
}
