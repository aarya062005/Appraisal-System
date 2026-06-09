package com.project.AppraisalSystem.repository;

import com.project.AppraisalSystem.dto.AppraisalsByEmployeeDTO;
import com.project.AppraisalSystem.entity.Appraisals;

import com.project.AppraisalSystem.entity.enums.AppraisalStatus;
import com.project.AppraisalSystem.entity.enums.CycleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface AppraisalsRepository extends JpaRepository<Appraisals, Long> {
    List<Appraisals> findAllByEmployee_UserId(Long employeeId);
    List<Appraisals> findAllByEmployee_Email(String email);
    List<Appraisals> findAllByManager_UserId(Long managerId);
    List<Appraisals> findAllByManager_Email(String email);
    List<Appraisals> findAllByCycleName(String cycleName);
    List<Appraisals> findAllByAppraisalStatus(AppraisalStatus appraisalStatus);
    List<Appraisals> findAllByCycleStatus(CycleStatus cycleStatus);
    boolean existsByCycleNameAndEmployee_UserId(String cycleName, Long employeeId);
    Optional<Appraisals> findByCycleNameAndEmployee_Email(String cycleName, String email);
}