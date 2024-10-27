package com.system.fcfs.prototype.domain.repository;

import com.system.fcfs.prototype.constant.Event;
import com.system.fcfs.prototype.domain.Winner;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface WinnerRepository {
    List<Winner> findAllWinners();

    Set<String> findWinnerByIdx(Long userIdx, Event event);

    void addQueue(String people, String now, Event event);

    List<Winner> geTop100Winner(Pageable pageable);
}
