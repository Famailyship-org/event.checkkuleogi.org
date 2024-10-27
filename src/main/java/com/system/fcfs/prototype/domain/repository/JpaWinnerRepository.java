package com.system.fcfs.prototype.domain.repository;

import com.system.fcfs.prototype.constant.Event;
import com.system.fcfs.prototype.domain.Winner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository("jpaWinnerRepository")
@RequiredArgsConstructor
public class JpaWinnerRepository implements WinnerRepository {

    private final WinnerJpaRepository winnerJpaRepository;

    @Override
    public List<Winner> findAllWinners() {
        return List.of();
    }

    @Override
    public void addQueue(String userIdx, String now, Event event) {
        winnerJpaRepository.save(Winner.builder()
                .winner(userIdx)
                .coupon(event.getName())
                .time(now)
                .build());
    }

    @Override
    public Set<String> findWinnerByIdx(Long userIdx, Event event) {
        return Set.of();
    }


    @Override
    public List<Winner> geTop100Winner(Pageable pageable){
        List<Winner> winners = winnerJpaRepository.findTop100(pageable);
        return winners;
    }

}
