package com.project.AppraisalSystem.service.implementation;

import com.project.AppraisalSystem.dto.GoalRequestDTO;
import com.project.AppraisalSystem.dto.GoalResponseDTO;
import com.project.AppraisalSystem.dto.GoalStatusDTO;
import com.project.AppraisalSystem.dto.GoalSummaryDTO;
import com.project.AppraisalSystem.entity.Appraisals;
import com.project.AppraisalSystem.entity.Goals;
import com.project.AppraisalSystem.entity.User;
import com.project.AppraisalSystem.entity.enums.GoalStatus;
import com.project.AppraisalSystem.exception.BadRequestException;
import com.project.AppraisalSystem.exception.ResourceNotFoundException;
import com.project.AppraisalSystem.repository.AppraisalsRepository;
import com.project.AppraisalSystem.repository.GoalsRepository;
import com.project.AppraisalSystem.repository.UserRepository;
import com.project.AppraisalSystem.service.GoalsService;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GoalsServiceImpl implements GoalsService {
    private final GoalsRepository goalRepository;
    private final AppraisalsRepository appraisalsRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    //------------HELPER ENTITY -GOAL RESPONSE DTO--------------------
    private GoalResponseDTO toResponseDTO(Goals goal) {
        GoalResponseDTO dto = modelMapper.map(goal, GoalResponseDTO.class);
        if (goal.getEmployee() != null) {
            dto.setEmployeeEmail(goal.getEmployee().getEmail());
            dto.setEmployeeName(goal.getEmployee().getFirstName()
                    + " " + goal.getEmployee().getLastName());
        }
        if (goal.getAppraisal() != null) {
            dto.setAppraisalId(goal.getAppraisal().getAppraisalId());
        }
        return dto;
    }
    //----------------------HELPER ENTITY -GOAL SUMMARY DTO----------------------------
    private GoalSummaryDTO toSummaryDTO(Goals goal) {
        GoalSummaryDTO dto = modelMapper.map(goal, GoalSummaryDTO.class);
        if (goal.getEmployee() != null) {
            dto.setEmployeeEmail(goal.getEmployee().getEmail());
            dto.setEmployeeName(goal.getEmployee().getFirstName()
                    + " " + goal.getEmployee().getLastName());
        }
        if (goal.getAppraisal() != null) {
            dto.setAppraisalId(goal.getAppraisal().getAppraisalId());
        }
        return dto;
    }
    //----------------HELPER ENTITY -GOAL STATUS DTO ---------------------------------
    private GoalStatusDTO toStatusDTO(Goals goal) {
        GoalStatusDTO dto = modelMapper.map(goal, GoalStatusDTO.class);
        if (goal.getEmployee() != null) {
            dto.setEmployeeEmail(goal.getEmployee().getEmail());
            dto.setEmployeeName(goal.getEmployee().getFirstName()
                    + " " + goal.getEmployee().getLastName());
        }
        return dto;
    }
    @Override
    @Transactional(readOnly = true)
    public List<GoalSummaryDTO> findAllGoals() {
        return goalRepository.findAll()
                .stream()
                .filter(goal -> goal.getGoalId() != null)
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public GoalResponseDTO findGoalById(Long goalId) {
        return goalRepository.findById(goalId)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + goalId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalSummaryDTO> findGoalsByAppraisal(Long appraisalId) {
        appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + appraisalId));

        return goalRepository.findAllByAppraisal_AppraisalId(appraisalId)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalResponseDTO> findGoalsByEmployee(Long employeeId) {
        userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId));

        return goalRepository.findAllByEmployee_UserId(employeeId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalResponseDTO> findGoalsByAppraisalAndEmployee(Long appraisalId, Long employeeId) {
        appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + appraisalId));

        userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId));

        return goalRepository.findAllByAppraisal_AppraisalIdAndEmployee_UserId(appraisalId, employeeId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalSummaryDTO> findGoalsByStatus(GoalStatus status) {
        return goalRepository.findAllByStatus(status)
                .stream()
                .map(this::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalResponseDTO> findGoalsByEmployeeAndStatus(Long employeeId, GoalStatus status) {
        userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId));

        return goalRepository.findAllByEmployee_UserIdAndStatus(employeeId, status)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalResponseDTO> findGoalsByEmployeeCompleted(Long employeeId, Boolean employeeCompleted) {
        userRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + employeeId));

        return goalRepository.findAllByEmployee_UserIdAndEmployeeCompleted(employeeId, employeeCompleted)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoalResponseDTO> findGoalsByAppraisalAndEmployeeCompleted(Long appraisalId, Boolean employeeCompleted) {
        appraisalsRepository.findById(appraisalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + appraisalId));

        return goalRepository.findAllByAppraisal_AppraisalIdAndEmployeeCompleted(appraisalId, employeeCompleted)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GoalResponseDTO createGoal(GoalRequestDTO dto) {

        Appraisals appraisal = appraisalsRepository.findById(dto.getAppraisalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appraisal not found with id: " + dto.getAppraisalId()));

        User employee = userRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with id: " + dto.getEmployeeId()));

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new BadRequestException("Goal title cannot be empty");
        }

        Goals goal = Goals.builder()
                .appraisal(appraisal)
                .employee(employee)
                .title(dto.getTitle().trim())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .build();

        return toResponseDTO(goalRepository.save(goal));
    }

    @Override
    @Transactional
    public GoalResponseDTO updateGoal(Long goalId, GoalRequestDTO dto) {

        Goals goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + goalId));

        if (goal.getStatus() == GoalStatus.CANCELLED) {
            throw new BadRequestException(
                    "Cannot update cancelled goal");
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            goal.setTitle(dto.getTitle().trim());
        }
        if (dto.getDescription() != null) {
            goal.setDescription(dto.getDescription());
        }
        if (dto.getDueDate() != null) {
            goal.setDueDate(dto.getDueDate());
        }

        return toResponseDTO(goalRepository.save(goal));
    }

    @Override
    @Transactional
    public GoalResponseDTO cancelGoal(Long goalId) {

        Goals goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + goalId));

        if (goal.getStatus() == GoalStatus.COMPLETED ||
                goal.getStatus() == GoalStatus.NOT_COMPLETED) {
            throw new BadRequestException(
                    "Cannot cancel goal in status: " + goal.getStatus());
        }

        goal.setStatus(GoalStatus.CANCELLED);
        return toResponseDTO(goalRepository.save(goal));
    }

    @Override
    @Transactional
    public GoalStatusDTO confirmGoalCompletion(Long goalId, GoalStatus status) {

        Goals goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + goalId));

        if (goal.getStatus() != GoalStatus.EMPLOYEE_SUBMITTED) {
            throw new BadRequestException(
                    "Cannot confirm goal in status: " + goal.getStatus());
        }

        if (status != GoalStatus.COMPLETED && status != GoalStatus.NOT_COMPLETED) {
            throw new BadRequestException(
                    "Status must be COMPLETED or NOT_COMPLETED");
        }

        goal.setStatus(status);
        return toStatusDTO(goalRepository.save(goal));
    }

    @Override
    @Transactional
    public GoalStatusDTO submitGoalCompletion(Long goalId, Boolean completed, String note) {

        Goals goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + goalId));

        if (goal.getStatus() == GoalStatus.CANCELLED) {
            throw new BadRequestException(
                    "Cannot submit cancelled goal");
        }

        if (goal.getStatus() == GoalStatus.COMPLETED ||
                goal.getStatus() == GoalStatus.NOT_COMPLETED) {
            throw new BadRequestException(
                    "Goal already confirmed by manager");
        }

        if (completed == null) {
            throw new BadRequestException(
                    "Completed field cannot be null");
        }

        goal.setEmployeeCompleted(completed);
        goal.setEmployeeNote(note);
        goal.setStatus(GoalStatus.EMPLOYEE_SUBMITTED);

        return toStatusDTO(goalRepository.save(goal));
    }

    @Override
    @Transactional
    public GoalStatusDTO updateEmployeeNote(Long goalId, String note) {

        Goals goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + goalId));

        if (goal.getStatus() != GoalStatus.EMPLOYEE_SUBMITTED) {
            throw new BadRequestException(
                    "Cannot update note in status: " + goal.getStatus());
        }

        if (note == null || note.isBlank()) {
            throw new BadRequestException(
                    "Note cannot be empty");
        }

        goal.setEmployeeNote(note.trim());
        return toStatusDTO(goalRepository.save(goal));
    }

    @Override
    @Transactional
    public String deleteGoal(Long goalId) {

        Goals goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Goal not found with id: " + goalId));

        if (goal.getStatus() != GoalStatus.NOT_STARTED) {
            throw new BadRequestException(
                    "Cannot delete goal in status: " + goal.getStatus()
                            + ". Only NOT_STARTED goals can be deleted");
        }

        goalRepository.delete(goal);
        return "Goal deleted successfully";
    }
}
