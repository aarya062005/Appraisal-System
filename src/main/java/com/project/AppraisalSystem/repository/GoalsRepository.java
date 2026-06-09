package com.project.AppraisalSystem.repository;

import com.project.AppraisalSystem.entity.Goals;
import com.project.AppraisalSystem.entity.enums.GoalStatus;
import org.hibernate.sql.exec.internal.JdbcParameterBindingImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalsRepository extends JpaRepository<Goals, Long> {
    List<Goals> findAllByAppraisal_AppraisalId(Long appraisalId);
    List<Goals> findAllByEmployee_UserId(Long employeeId);
    List<Goals> findAllByAppraisal_AppraisalIdAndEmployee_UserId(Long appraisalId, Long employeeId);
    List<Goals> findAllByStatus(GoalStatus status);
    List<Goals> findAllByEmployee_UserIdAndStatus(Long employeeId, GoalStatus status);
    List<Goals> findAllByEmployee_UserIdAndEmployeeCompleted(Long employeeId, Boolean employeeCompleted);
    List<Goals> findAllByAppraisal_AppraisalIdAndEmployeeCompleted(Long appraisalId, Boolean employeeCompleted);
}
