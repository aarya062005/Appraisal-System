package com.project.AppraisalSystem.service;

import com.project.AppraisalSystem.dto.GoalRequestDTO;
import com.project.AppraisalSystem.dto.GoalResponseDTO;
import com.project.AppraisalSystem.dto.GoalStatusDTO;
import com.project.AppraisalSystem.dto.GoalSummaryDTO;
import com.project.AppraisalSystem.entity.enums.GoalStatus;

import java.util.List;

public interface GoalsService {
    //------------------GET METHOD -----------------------------------------------
    List<GoalSummaryDTO> findAllGoals();
    GoalResponseDTO findGoalById(Long goalId);
    List<GoalSummaryDTO> findGoalsByAppraisal(Long appraisalId);
    List<GoalResponseDTO> findGoalsByEmployee(Long employeeId);
    List<GoalResponseDTO> findGoalsByAppraisalAndEmployee(Long appraisalId, Long employeeId);
    List<GoalSummaryDTO> findGoalsByStatus(GoalStatus status);
    List<GoalResponseDTO> findGoalsByEmployeeAndStatus(Long employeeId, GoalStatus status);
    List<GoalResponseDTO> findGoalsByEmployeeCompleted(Long employeeId, Boolean employeeCompleted);
    List<GoalResponseDTO> findGoalsByAppraisalAndEmployeeCompleted(Long appraisalId, Boolean employeeCompleted);

    //-----------------CREATE METHOD ----------------------------------------------
    GoalResponseDTO createGoal(GoalRequestDTO dto);

    //--------------------UPDATE METHOD--------------------------------------------------

    GoalResponseDTO updateGoal(Long goalId, GoalRequestDTO dto);
    GoalResponseDTO cancelGoal(Long goalId);
    GoalStatusDTO confirmGoalCompletion(Long goalId, GoalStatus status);
    GoalStatusDTO submitGoalCompletion(Long goalId, Boolean completed, String note);
    GoalStatusDTO updateEmployeeNote(Long goalId, String note);

    //-------------------DELETE METHOD -------------------------------------
    String deleteGoal(Long goalId);
}
