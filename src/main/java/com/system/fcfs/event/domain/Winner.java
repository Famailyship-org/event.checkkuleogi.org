package com.system.fcfs.event.domain;

import com.system.fcfs.event.constant.Event;
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
    @Column(name = "idx")
    private Long idx;

    private String winner;
    private String timeStamp;

    @Column(name = "user_rank")
    private Long rank;
    private String event;
}
