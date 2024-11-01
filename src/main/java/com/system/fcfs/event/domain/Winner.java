package com.system.fcfs.event.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Winner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "winner_idx")
    private Long idx;
    // 당첨자는 하나의 응모 시도와 일대일 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_idx", nullable = false)
    private Attempt attempt;

    @Column(name = "timestamp", nullable = false)
    private String timeStamp;

    @JoinColumn(name = "event_idx", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
}
