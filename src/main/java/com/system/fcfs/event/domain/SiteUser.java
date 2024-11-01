package com.system.fcfs.event.domain;

import com.system.fcfs.event.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "user")
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long idx;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_id", nullable = false, unique = true)
    private String id;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_birth")
    private LocalDate birth;

    @Column(name = "user_gender")
    private String gender;

    @Column(name = "user_email", unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Role role;

    public void setId(Long userId) {
        this.idx = userId;
    }

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "attempt_idx", nullable = false)
//    private Attempt attempt;

}