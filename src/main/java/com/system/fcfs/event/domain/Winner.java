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
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "attempt_idx", nullable = false)
    @Column(name = "attempt_idx", nullable = false)
    private Long attemptIdx;

    @Column(name = "timestamp", nullable = false)
    private String timeStamp;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "phone_num", nullable = false)
    private String phoneNum;

    //    @JoinColumn(name = "event_idx", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
    private String eventName;
}
