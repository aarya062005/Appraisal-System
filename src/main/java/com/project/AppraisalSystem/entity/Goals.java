package com.project.AppraisalSystem.entity;

import com.project.AppraisalSystem.entity.enums.GoalStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "goals")
public class Goals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appraisal_id", nullable = false)
    private Appraisals appraisal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private User employee;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private GoalStatus status;

    @Column(name = "employee_completed")
    private Boolean employeeCompleted;

    @Column(name = "employee_note", columnDefinition = "TEXT")
    private String employeeNote;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @PrePersist
    protected void onCreate() {
        this.status = GoalStatus.NOT_STARTED;

    }
}
