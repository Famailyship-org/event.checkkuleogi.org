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
@Table(name = "attempt")
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attempt_idx")
    private Long idx;

//     응모 시도는 하나의 이벤트와 다대일 관계
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "event_idx", nullable = false)
//     private Event event;

    @Column(name = "timestamp", nullable = false)
    private String timeStamp;

    @Column(name = "phone_num", nullable = false)
    private String phoneNum;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "user_name", nullable = false)
    private String userName;
}
