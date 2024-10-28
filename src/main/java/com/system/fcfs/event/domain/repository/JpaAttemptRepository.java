package com.system.fcfs.event.domain.repository;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.dto.PostEventRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository("jpaWinnerRepository")
@RequiredArgsConstructor
public class JpaAttemptRepository implements AttemptRepository {

    private final AtomicInteger rankCounter = new AtomicInteger(1);
    private final AttemptJpaRepository attemptJpaRepository;

    @Override
    public List<Attempt> geTop100Attempt(Pageable pageable) {
        return attemptJpaRepository.findTop100(pageable);
    }

    @Override
    public boolean existsByAttemptAndEvent(PostEventRequestDTO postEventRequestDTO) {
        return attemptJpaRepository.existsByUserId(postEventRequestDTO.getUserId());
    }

    @Transactional
    @Override
    public void addQueue(PostEventRequestDTO postEventRequestDTO) {
        postEventRequestDTO.setRank((long) rankCounter.getAndIncrement());
        attemptJpaRepository.save(Attempt.builder()
                .userName(postEventRequestDTO.getUserId())
                .coupon(postEventRequestDTO.getEventName())
                .rank(postEventRequestDTO.getRank())
                .timeStamp(postEventRequestDTO.getTimestamp())
                .build());
    }
}
