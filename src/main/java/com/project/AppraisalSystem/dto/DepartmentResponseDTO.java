package com.project.AppraisalSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDTO {


    private Long deptId;


    private String deptName;


    private String deptDescription;


    private Integer employeeCount;

}