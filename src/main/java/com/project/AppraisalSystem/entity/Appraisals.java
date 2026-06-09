package com.project.AppraisalSystem.entity;


import com.project.AppraisalSystem.entity.enums.AppraisalStatus;
import com.project.AppraisalSystem.entity.enums.CycleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "appraisals",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cycle_name", "employee_id"}))
public class Appraisals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appraisalId;

    @Version
    private Long version;

    @Column(name = "cycle_name", nullable = false)
    private String cycleName;

    @Column(name = "cycle_start_date", nullable = false)
    private LocalDate cycleStartDate;

    @Column(name = "cycle_end_date", nullable = false)
    private LocalDate cycleEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_status", nullable = false)
    private CycleStatus cycleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @Column(name = "what_went_well", columnDefinition = "TEXT")
    private String whatWentWell;

    @Column(name = "what_to_improve", columnDefinition = "TEXT")
    private String whatToImprove;

    @Column(name = "achievements", columnDefinition = "TEXT")
    private String achievements;

    @Column(name = "self_rating")
    private Integer selfRating;

    @Column(name = "manager_strengths", columnDefinition = "TEXT")
    private String managerStrengths;

    @Column(name = "manager_improve", columnDefinition = "TEXT")
    private String managerImprove;

    @Column(name = "manager_comments", columnDefinition = "TEXT")
    private String managerComments;

    @Column(name = "manager_rating")
    private Integer managerRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "appraisal_status", nullable = false)
    private AppraisalStatus appraisalStatus;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.appraisalStatus = AppraisalStatus.PENDING;
        this.cycleStatus = CycleStatus.DRAFT;
        this.version = 0L;
    }
}