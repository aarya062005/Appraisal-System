package com.project.AppraisalSystem.repository;

import com.project.AppraisalSystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long>{

Optional<Department> findByDeptName(String deptName);



}
