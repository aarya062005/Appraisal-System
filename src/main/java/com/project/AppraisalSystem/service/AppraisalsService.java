package com.project.AppraisalSystem.service;

import com.project.AppraisalSystem.dto.*;
import com.project.AppraisalSystem.entity.enums.AppraisalStatus;
import com.project.AppraisalSystem.entity.enums.CycleStatus;

import java.util.List;

public interface AppraisalsService {

    //-------------------Get Method ----------------------------------------------------------------------

    List<AppraisalsSummaryDTO> findAllAppraisals();
    AppraisalsSummaryDTO findAppraisalById(Long appraisalId);
    List<AppraisalsByEmployeeDTO> findAppraisalsByEmployee_Id(Long employeeId);
    List<AppraisalsByEmployeeDTO> findAppraisalsByEmployeeEmail(String email);
    List<AppraisalsByManagerDTO> findAppraisalsByManager_Id(Long managerId);
    List<AppraisalsByManagerDTO> findAppraisalsByManagerEmail(String email);
    List<AppraisalsSummaryDTO> findAppraisalsByCycle(String cycleName);
    List<AppraisalsSummaryDTO> findAppraisalsByStatus(AppraisalStatus status);
    List<AppraisalsSummaryDTO> findAppraisalsByCycleStatus(CycleStatus cycleStatus);

    //-------------------------Create-----------------------------------------------------
    AppraisalsSummaryDTO createAppraisal(AppraisalsRequestDTO dto);

    //------------------------------SELF ASSESMENT -----------------------------------
    EmployeeAppraisalResponseDTO saveSelfAssessmentDraft(Long appraisalId, SelfAssessmentDTO dto);
    EmployeeAppraisalResponseDTO saveSelfAssessmentDraftByEmployeeEmail(String employeeEmail, String cycleName, SelfAssessmentDTO dto);
    EmployeeAppraisalResponseDTO submitSelfAssessment(Long appraisalId, SelfAssessmentDTO dto);
    EmployeeAppraisalResponseDTO submitSelfAssessmentByEmployeeEmail(String employeeEmail, String cycleName, SelfAssessmentDTO dto);

    // ---------------------- MANAGER REVIEW-------------------------------------------------
    ManagerAppraisalResponseDTO saveManagerReviewDraft(Long appraisalId, ManagerReviewDTO dto);
    ManagerAppraisalResponseDTO submitManagerReview(Long appraisalId, ManagerReviewDTO dto);
    ManagerAppraisalResponseDTO saveManagerReviewDraftByEmployeeEmail(String employeeEmail, String cycleName, ManagerReviewDTO dto);
    ManagerAppraisalResponseDTO submitManagerReviewByEmployeeEmail(String employeeEmail, String cycleName, ManagerReviewDTO dto);

    // -----------------------------------HR ----------------------------------------------
    AppraisalsSummaryDTO approveAppraisal(Long appraisalId);

    // -------------------------------------- ---------------------------------------------
    EmployeeAppraisalResponseDTO acknowledgeAppraisal(Long appraisalId);
    // -------------------------------------------DELETE-------------------------------------------
    String deleteAppraisal(Long appraisalId);

    EmployeeAppraisalResponseDTO    findAppraisalByIdForEmployee(Long appraisalId);
};