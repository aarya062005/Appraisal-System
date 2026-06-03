package com.project.AppraisalSystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;
   @Column(name = "dept_name")
    private String deptName;
   @Column(name = "dept_description")
   private String deptDescription;
}
