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
    @Column(name = "idx")
    private Long idx;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "coupon_id")
    private String coupon;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "winner_id")
    private String userName;
    private String timeStamp;
    @Column(name = "user_rank")
    private Long rank;
}
