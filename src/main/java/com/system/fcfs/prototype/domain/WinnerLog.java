package com.system.fcfs.prototype.domain;

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
public class WinnerLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_winning_log_id")
    private Long id;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "coupon_id")
    private String coupon;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "winner_id")
    private String winner;
}
