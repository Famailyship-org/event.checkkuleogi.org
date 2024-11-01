package com.system.fcfs.event.domain;

import com.system.fcfs.event.domain.enums.EventType;
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
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_idx")
    private Long idx;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;
}
