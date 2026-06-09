package com.project.AppraisalSystem.service.implementation;

import com.project.AppraisalSystem.dto.*;
import com.project.AppraisalSystem.entity.Appraisals;
import com.project.AppraisalSystem.entity.User;
import com.project.AppraisalSystem.entity.enums.AppraisalStatus;
import com.project.AppraisalSystem.entity.enums.CycleStatus;
import com.project.AppraisalSystem.entity.enums.Roles;
import com.project.AppraisalSystem.exception.BadRequestException;
import com.project.AppraisalSystem.exception.DuplicateResourceException;
import com.project.AppraisalSystem.exception.ResourceNotFoundException;
import com.project.AppraisalSystem.repository.AppraisalsRepository;
import com.project.AppraisalSystem.repository.DepartmentRepository;
import com.project.AppraisalSystem.repository.UserRepository;
import com.project.AppraisalSystem.service.AppraisalsService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppraisalsServiceImpl implements AppraisalsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AppraisalsRepository appraisalsRepository;

    //--------------------------------MAPPER FOR SUMMARY----------------------------------------------
    private AppraisalsSummaryDTO toSummaryDTO(Appraisals appraisal) {
        AppraisalsSummaryDTO dto = modelMapper.map(appraisal, AppraisalsSummaryDTO.class);
        if (appraisal.getEmployee() != null) {
            dto.setEmployeeEmail(appraisal.getEmployee().getEmail());
            dto.setEmployeeEmail(appraisal.getEmployee().getFirstName()
                    + " " + appraisal.getEmployee().getLastName());
        }
        if (appraisal.getManager() != null) {
            dto.setManagerEmail(appraisal.getManager().getEmail());
            dto.setManagerEmail(appraisal.getManager().getFirstName()
                    + " " + appraisal.getManager().getLastName());
        }
        return dto;
    }
    //-----------------------------------MAPPER FOR EMPLOYEE SEARCHING--------------------------------------
    private AppraisalsByEmployeeDTO toEmployeeDTO(Appraisals appraisal) {
        AppraisalsByEmployeeDTO dto = modelMapper.map(appraisal, AppraisalsByEmployeeDTO.class);
        if (appraisal.getManager() != null) {
            dto.setManagerEmail(appraisal.getManager().getEmail());
        }
        return dto;
    }

    // ---------------------------------------MAPPER FOR MANAGER-------------------------------------------
    private AppraisalsByManagerDTO toManagerDTO(Appraisals appraisal) {
        AppraisalsByManagerDTO dto = modelMapper.map(appraisal, AppraisalsByManagerDTO.class);
        if (appraisal.getEmployee() != null) {
            dto.setEmployeeEmail(appraisal.getEmployee().getEmail());
        }
        return dto;
    }
    // ------------------------------------EMPLOYEE RESPONSE--------------------------------------------
    private EmployeeAppraisalResponseDTO toEmployeeResponseDTO(Appraisals appraisal) {
        EmployeeAppraisalResponseDTO dto = modelMapper.map(appraisal, EmployeeAppraisalResponseDTO.class);
        if (appraisal.getManager() != null) {
            dto.setManagerEmail(appraisal.getManager().getEmail());
            dto.setManagerName(appraisal.getManager().getFirstName()
                    + " " + appraisal.getManager().getLastName());
        }
        if (appraisal.getAppraisalStatus() != AppraisalStatus.APPROVED &&
                appraisal.getAppraisalStatus() != AppraisalStatus.ACKNOWLEDGED) {
            dto.setManagerStrengths(null);
            dto.setManagerImprove(null);
            dto.setManagerComments(null);
            dto.setManagerRating(null);
            dto.setApprovedAt(null);
        }
        return dto;
    }

    // --------------------------------------------MANAGER RESPONSE----------------------------------------
    private ManagerAppraisalResponseDTO toManagerResponseDTO(Appraisals appraisal) {
        ManagerAppraisalResponseDTO dto = modelMapper.map(appraisal, ManagerAppraisalResponseDTO.class);
        if (appraisal.getEmployee() != null) {
            dto.setEmployeeEmail(appraisal.getEmployee().getEmail());
            dto.setEmployeeName(appraisal.getEmployee().getFirstName()
                    + " " + appraisal.getEmployee().getLastName());
        }
        if (appraisal.getAppraisalStatus() == AppraisalStatus.PENDING ||
                appraisal.getAppraisalStatus() == AppraisalStatus.EMPLOYEE_DRAFT) {
            dto.setWhatWentWell(null);
            dto.setWhatToImprove(null);
            dto.setAchievements(null);
            dto.setSelfRating(null);
        }
        return dto;
    }
    //--------------------------GET ALL APPRAISAL-------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<AppraisalsSummaryDTO> findAllAppraisals() {
        return appraisalsRepository.findAll()
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    //--------------------------GET BY APPRAISAL ID-----------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public AppraisalsSummaryDTO findAppraisalById(Long appraisalId) {
        Appraisals appraisal = appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException("Appraisal not found with id: " + appraisalId));
        return toSummaryDTO(appraisal);
    }

    //------------------------------GET APPRAISAL BY EMPLOYEE ID--------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<AppraisalsByEmployeeDTO> findAppraisalsByEmployee_Id(Long employeeId) {
        return appraisalsRepository.findAllByEmployee_UserId(employeeId)
                .stream()
                .map(this::toEmployeeDTO)
                .collect(Collectors.toList());
    }

    //-------------------------------GET APPRAISAL BY EMPLOYEE EMAIL----------------------------
    @Override
    @Transactional(readOnly = true)
    public List<AppraisalsByEmployeeDTO> findAppraisalsByEmployeeEmail(String email) {
        return appraisalsRepository.findAllByEmployee_Email(email)
                .stream()
                .map(this::toEmployeeDTO)
                .collect(Collectors.toList());
    }

    //-----------------------------------GET APPRAISAL BY MANAGER ID---------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<AppraisalsByManagerDTO> findAppraisalsByManager_Id(Long managerId) {
        return appraisalsRepository.findAllByManager_UserId(managerId)
                .stream()
                .map(this::toManagerDTO)
                .collect(Collectors.toList());
    }

    //----------------------------GET APPRAISAL BY MANAGER EMAIL---------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<AppraisalsByManagerDTO> findAppraisalsByManagerEmail(String email) {
        return appraisalsRepository.findAllByManager_Email(email)
                .stream()
                .map(this::toManagerDTO)
                .collect(Collectors.toList());
    }

    //---------------------------GET APPRAISAL BY CYCLE NAME -----------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<AppraisalsSummaryDTO> findAppraisalsByCycle(String cycleName) {
        return appraisalsRepository.findAllByCycleName(cycleName)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    //------------------------------GET APPRAISAL BY APPRAISAL STATUS --------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<AppraisalsSummaryDTO> findAppraisalsByStatus(AppraisalStatus status) {
        return appraisalsRepository.findAllByAppraisalStatus(status)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    //------------------------------GET APPRAISAL BY CYCLE STATUS --------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<AppraisalsSummaryDTO> findAppraisalsByCycleStatus(CycleStatus cycleStatus) {
        return appraisalsRepository.findAllByCycleStatus(cycleStatus)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    //------------------------------POST APPRAISAL --------------------------------
    @Override
    public AppraisalsSummaryDTO createAppraisal(AppraisalsRequestDTO dto) {

        User employee = userRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + dto.getEmployeeId()));

        User manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Manager not found with id: " + dto.getManagerId()));

        if (!manager.getRole().equals(Roles.MANAGER)) {
            throw new BadRequestException(
                    "User with id: " + dto.getManagerId() + " is not a manager");
        }

        if (appraisalsRepository.existsByCycleNameAndEmployee_UserId(
                dto.getCycleName(), dto.getEmployeeId())) {
            throw new DuplicateResourceException(
                    "Appraisal already exists for employee: "
                            + dto.getEmployeeId() + " in cycle: " + dto.getCycleName());
        }

        if (!dto.getCycleStartDate().isBefore(dto.getCycleEndDate())) {
            throw new BadRequestException(
                    "Cycle start date must be before end date");
        }

        Appraisals appraisal = Appraisals.builder()
                .cycleName(dto.getCycleName())
                .cycleStartDate(dto.getCycleStartDate())
                .cycleEndDate(dto.getCycleEndDate())
                .employee(employee)
                .manager(manager)
                .build();

        return toSummaryDTO(appraisalsRepository.save(appraisal));
    }

    //-----------------------------------SAVING SELF ASSESSMENT DRAFT-------------------------------------------
    @Override
    public EmployeeAppraisalResponseDTO saveSelfAssessmentDraft(Long appraisalId, SelfAssessmentDTO dto) {

        Appraisals appraisal = appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + appraisalId));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.PENDING &&
                appraisal.getAppraisalStatus() != AppraisalStatus.EMPLOYEE_DRAFT) {
            throw new BadRequestException(
                    "Cannot edit appraisal in status: " + appraisal.getAppraisalStatus());
        }

        appraisal.setWhatWentWell(dto.getWhatWentWell());
        appraisal.setWhatToImprove(dto.getWhatToImprove());
        appraisal.setAchievements(dto.getAchievements());
        appraisal.setSelfRating(dto.getSelfRating());
        appraisal.setAppraisalStatus(AppraisalStatus.EMPLOYEE_DRAFT);

        return toEmployeeResponseDTO(appraisalsRepository.save(appraisal));
    }

    // ------------------------SAVING SELF ASSESSMENT DRAFT THROUGH EMAIL--------------------------
    @Override
    public EmployeeAppraisalResponseDTO saveSelfAssessmentDraftByEmployeeEmail(
            String employeeEmail, String cycleName, SelfAssessmentDTO dto) {

        Appraisals appraisal = appraisalsRepository
                .findByCycleNameAndEmployee_Email(cycleName, employeeEmail.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found for email: " + employeeEmail
                                + " in cycle: " + cycleName));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.PENDING &&
                appraisal.getAppraisalStatus() != AppraisalStatus.EMPLOYEE_DRAFT) {
            throw new BadRequestException(
                    "Cannot edit appraisal in status: " + appraisal.getAppraisalStatus());
        }

        appraisal.setWhatWentWell(dto.getWhatWentWell());
        appraisal.setWhatToImprove(dto.getWhatToImprove());
        appraisal.setAchievements(dto.getAchievements());
        appraisal.setSelfRating(dto.getSelfRating());
        appraisal.setAppraisalStatus(AppraisalStatus.EMPLOYEE_DRAFT);

        return toEmployeeResponseDTO(appraisalsRepository.save(appraisal));
    }

    // ------------------------SUBMIT SELF ASSESSMENT  --------------------------
    @Override
    public EmployeeAppraisalResponseDTO submitSelfAssessment(Long appraisalId, SelfAssessmentDTO dto) {

        Appraisals appraisal = appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + appraisalId));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.PENDING &&
                appraisal.getAppraisalStatus() != AppraisalStatus.EMPLOYEE_DRAFT) {
            throw new BadRequestException(
                    "Cannot submit appraisal in status: " + appraisal.getAppraisalStatus());
        }

        if (dto.getWhatWentWell() == null || dto.getWhatWentWell().isBlank()) {
            throw new BadRequestException("What went well cannot be empty");
        }
        if (dto.getWhatToImprove() == null || dto.getWhatToImprove().isBlank()) {
            throw new BadRequestException("What to improve cannot be empty");
        }
        if (dto.getAchievements() == null || dto.getAchievements().isBlank()) {
            throw new BadRequestException("Achievements cannot be empty");
        }
        if (dto.getSelfRating() == null) {
            throw new BadRequestException("Self rating cannot be empty");
        }
        if (dto.getSelfRating() < 1 || dto.getSelfRating() > 5) {
            throw new BadRequestException("Self rating must be between 1 and 5");
        }

        appraisal.setWhatWentWell(dto.getWhatWentWell());
        appraisal.setWhatToImprove(dto.getWhatToImprove());
        appraisal.setAchievements(dto.getAchievements());
        appraisal.setSelfRating(dto.getSelfRating());
        appraisal.setAppraisalStatus(AppraisalStatus.SELF_SUBMITTED);
        appraisal.setSubmittedAt(LocalDateTime.now());

        return toEmployeeResponseDTO(appraisalsRepository.save(appraisal));
    }
    // ------------------------SUBMIT SELF ASSESSMENT THROUGH EMAIL--------------------------
    @Override
    public EmployeeAppraisalResponseDTO submitSelfAssessmentByEmployeeEmail(
            String employeeEmail, String cycleName, SelfAssessmentDTO dto) {

        Appraisals appraisal = appraisalsRepository
                .findByCycleNameAndEmployee_Email(cycleName, employeeEmail.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found for email: " + employeeEmail
                                + " in cycle: " + cycleName));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.PENDING &&
                appraisal.getAppraisalStatus() != AppraisalStatus.EMPLOYEE_DRAFT) {
            throw new BadRequestException(
                    "Cannot submit appraisal in status: " + appraisal.getAppraisalStatus());
        }

        if (dto.getWhatWentWell() == null || dto.getWhatWentWell().isBlank()) {
            throw new BadRequestException("What went well cannot be empty");
        }
        if (dto.getWhatToImprove() == null || dto.getWhatToImprove().isBlank()) {
            throw new BadRequestException("What to improve cannot be empty");
        }
        if (dto.getAchievements() == null || dto.getAchievements().isBlank()) {
            throw new BadRequestException("Achievements cannot be empty");
        }
        if (dto.getSelfRating() == null) {
            throw new BadRequestException("Self rating cannot be empty");
        }
        if (dto.getSelfRating() < 1 || dto.getSelfRating() > 5) {
            throw new BadRequestException("Self rating must be between 1 and 5");
        }

        appraisal.setWhatWentWell(dto.getWhatWentWell());
        appraisal.setWhatToImprove(dto.getWhatToImprove());
        appraisal.setAchievements(dto.getAchievements());
        appraisal.setSelfRating(dto.getSelfRating());
        appraisal.setAppraisalStatus(AppraisalStatus.SELF_SUBMITTED);
        appraisal.setSubmittedAt(LocalDateTime.now());

        return toEmployeeResponseDTO(appraisalsRepository.save(appraisal));
    }
    @Override
    public ManagerAppraisalResponseDTO saveManagerReviewDraft(Long appraisalId, ManagerReviewDTO dto) {

        Appraisals appraisal = appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + appraisalId));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.SELF_SUBMITTED &&
                appraisal.getAppraisalStatus() != AppraisalStatus.MANAGER_DRAFT) {
            throw new BadRequestException(
                    "Cannot edit appraisal in status: " + appraisal.getAppraisalStatus());
        }

        appraisal.setManagerStrengths(dto.getManagerStrengths());
        appraisal.setManagerImprove(dto.getManagerImprove());
        appraisal.setManagerComments(dto.getManagerComments());
        appraisal.setManagerRating(dto.getManagerRating());
        appraisal.setAppraisalStatus(AppraisalStatus.MANAGER_DRAFT);

        return toManagerResponseDTO(appraisalsRepository.save(appraisal));
    }

    @Override
    public ManagerAppraisalResponseDTO saveManagerReviewDraftByEmployeeEmail(
            String employeeEmail, String cycleName, ManagerReviewDTO dto) {

        Appraisals appraisal = appraisalsRepository
                .findByCycleNameAndEmployee_Email(cycleName, employeeEmail.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found for email: " + employeeEmail
                                + " in cycle: " + cycleName));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.SELF_SUBMITTED &&
                appraisal.getAppraisalStatus() != AppraisalStatus.MANAGER_DRAFT) {
            throw new BadRequestException(
                    "Cannot edit appraisal in status: " + appraisal.getAppraisalStatus());
        }

        appraisal.setManagerStrengths(dto.getManagerStrengths());
        appraisal.setManagerImprove(dto.getManagerImprove());
        appraisal.setManagerComments(dto.getManagerComments());
        appraisal.setManagerRating(dto.getManagerRating());
        appraisal.setAppraisalStatus(AppraisalStatus.MANAGER_DRAFT);

        return toManagerResponseDTO(appraisalsRepository.save(appraisal));
    }

    @Override
    public ManagerAppraisalResponseDTO submitManagerReview(Long appraisalId, ManagerReviewDTO dto) {

        Appraisals appraisal = appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + appraisalId));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.SELF_SUBMITTED &&
                appraisal.getAppraisalStatus() != AppraisalStatus.MANAGER_DRAFT) {
            throw new BadRequestException(
                    "Cannot submit appraisal in status: " + appraisal.getAppraisalStatus());
        }

        if (dto.getManagerStrengths() == null || dto.getManagerStrengths().isBlank()) {
            throw new BadRequestException("Manager strengths cannot be empty");
        }
        if (dto.getManagerImprove() == null || dto.getManagerImprove().isBlank()) {
            throw new BadRequestException("Manager improvements cannot be empty");
        }
        if (dto.getManagerRating() == null) {
            throw new BadRequestException("Manager rating cannot be empty");
        }
        if (dto.getManagerRating() < 1 || dto.getManagerRating() > 5) {
            throw new BadRequestException("Manager rating must be between 1 and 5");
        }

        appraisal.setManagerStrengths(dto.getManagerStrengths());
        appraisal.setManagerImprove(dto.getManagerImprove());
        appraisal.setManagerComments(dto.getManagerComments());
        appraisal.setManagerRating(dto.getManagerRating());
        appraisal.setAppraisalStatus(AppraisalStatus.MANAGER_REVIEWED);

        return toManagerResponseDTO(appraisalsRepository.save(appraisal));
    }

    @Override
    public ManagerAppraisalResponseDTO submitManagerReviewByEmployeeEmail(
            String employeeEmail, String cycleName, ManagerReviewDTO dto) {

        Appraisals appraisal = appraisalsRepository
                .findByCycleNameAndEmployee_Email(cycleName, employeeEmail.trim())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found for email: " + employeeEmail
                                + " in cycle: " + cycleName));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.SELF_SUBMITTED &&
                appraisal.getAppraisalStatus() != AppraisalStatus.MANAGER_DRAFT) {
            throw new BadRequestException(
                    "Cannot submit appraisal in status: " + appraisal.getAppraisalStatus());
        }

        if (dto.getManagerStrengths() == null || dto.getManagerStrengths().isBlank()) {
            throw new BadRequestException("Manager strengths cannot be empty");
        }
        if (dto.getManagerImprove() == null || dto.getManagerImprove().isBlank()) {
            throw new BadRequestException("Manager improvements cannot be empty");
        }
        if (dto.getManagerRating() == null) {
            throw new BadRequestException("Manager rating cannot be empty");
        }
        if (dto.getManagerRating() < 1 || dto.getManagerRating() > 5) {
            throw new BadRequestException("Manager rating must be between 1 and 5");
        }

        appraisal.setManagerStrengths(dto.getManagerStrengths());
        appraisal.setManagerImprove(dto.getManagerImprove());
        appraisal.setManagerComments(dto.getManagerComments());
        appraisal.setManagerRating(dto.getManagerRating());
        appraisal.setAppraisalStatus(AppraisalStatus.MANAGER_REVIEWED);

        return toManagerResponseDTO(appraisalsRepository.save(appraisal));
    }
    @Override
    public AppraisalsSummaryDTO approveAppraisal(Long appraisalId) {

        Appraisals appraisal = appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + appraisalId));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.MANAGER_REVIEWED) {
            throw new BadRequestException(
                    "Cannot approve appraisal in status: " + appraisal.getAppraisalStatus());
        }

        appraisal.setAppraisalStatus(AppraisalStatus.APPROVED);
        appraisal.setApprovedAt(LocalDateTime.now());

        return toSummaryDTO(appraisalsRepository.save(appraisal));
    }
    @Override
    public EmployeeAppraisalResponseDTO acknowledgeAppraisal(Long appraisalId) {

        Appraisals appraisal = appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + appraisalId));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.APPROVED) {
            throw new BadRequestException(
                    "Cannot acknowledge appraisal in status: " + appraisal.getAppraisalStatus());
        }

        appraisal.setAppraisalStatus(AppraisalStatus.ACKNOWLEDGED);

        return toEmployeeResponseDTO(appraisalsRepository.save(appraisal));
    }

    @Override
    public String deleteAppraisal(Long appraisalId) {

        Appraisals appraisal = appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + appraisalId));

        if (appraisal.getAppraisalStatus() != AppraisalStatus.PENDING) {
            throw new BadRequestException(
                    "Cannot delete appraisal in status: " + appraisal.getAppraisalStatus()
                            + ". Only PENDING appraisals can be deleted");
        }

        appraisalsRepository.delete(appraisal);
        return "Appraisal deleted successfully";
    }




}