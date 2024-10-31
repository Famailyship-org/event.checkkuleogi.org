package com.system.fcfs.event.repository;

import com.system.fcfs.event.domain.Attempt;
import com.system.fcfs.event.domain.Winner;
import com.system.fcfs.event.dto.request.PostEventRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jpaWinnerRepository")
@RequiredArgsConstructor
public class JpaAttemptRepository implements AttemptRepository {

    private final AttemptJpaRepository attemptJpaRepository;

    @Override
    public List<Winner> geTop100Attempt(Pageable pageable, String eventName) {
        return attemptJpaRepository.findTop100(pageable);
    }

    @Override
    public boolean existsByAttemptAndEvent(PostEventRequestDTO postEventRequestDTO) {
        return attemptJpaRepository.existsByUserId(postEventRequestDTO.getUserId());
    }

    @Override
    public void addQueue(PostEventRequestDTO postEventRequestDTO) {
        attemptJpaRepository.save(Attempt.builder()
                .userName(postEventRequestDTO.getUserId())
                .coupon(postEventRequestDTO.getEventName())
                .rank(postEventRequestDTO.getRank())
                .timeStamp(postEventRequestDTO.getTimestamp())
                .build());
    }
}
