package com.project.AppraisalSystem.service.implementation;

import com.project.AppraisalSystem.dto.CycleDetailDTO;
import com.project.AppraisalSystem.dto.CycleReportDTO;
import com.project.AppraisalSystem.entity.Appraisals;
import com.project.AppraisalSystem.entity.enums.AppraisalStatus;
import com.project.AppraisalSystem.exception.ResourceNotFoundException;
import com.project.AppraisalSystem.repository.AppraisalsRepository;
import com.project.AppraisalSystem.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final AppraisalsRepository appraisalsRepository;

    @Override
    public List<String> findAllCycleNames() {
        return appraisalsRepository.findAll()
                .stream()
                .map(Appraisals::getCycleName)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    private List<Appraisals> getCycleOrThrow(String cycleName) {
        List<Appraisals> appraisals = appraisalsRepository.findAllByCycleName(cycleName);
        if (appraisals.isEmpty()) {
            throw new ResourceNotFoundException("No appraisals found for cycle: " + cycleName);
        }
        return appraisals;
    }

    @Override
    public CycleReportDTO getCycleReport(String cycleName) {
        List<Appraisals> appraisals = getCycleOrThrow(cycleName);

        int total = appraisals.size();
        long acknowledged = appraisals.stream()
                .filter(a -> a.getAppraisalStatus() == AppraisalStatus.ACKNOWLEDGED)
                .count();
        long pendingAction = total - acknowledged;
        int completion = total == 0 ? 0 : (int) Math.round((acknowledged * 100.0) / total);

        double avgSelfRating = appraisals.stream()
                .map(Appraisals::getSelfRating)
                .filter(r -> r != null && r > 0)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        double avgManagerRating = appraisals.stream()
                .map(Appraisals::getManagerRating)
                .filter(r -> r != null && r > 0)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        Map<AppraisalStatus, Long> grouped = appraisals.stream()
                .collect(Collectors.groupingBy(Appraisals::getAppraisalStatus, Collectors.counting()));

        Map<String, Integer> statusBreakdown = new LinkedHashMap<>();
        for (AppraisalStatus status : AppraisalStatus.values()) {
            statusBreakdown.put(status.name(), grouped.getOrDefault(status, 0L).intValue());
        }

        return CycleReportDTO.builder()
                .cycleName(cycleName)
                .totalAppraisals(total)
                .acknowledged((int) acknowledged)
                .completion(completion)
                .pendingAction((int) pendingAction)
                .avgSelfRating(avgSelfRating)
                .avgManagerRating(avgManagerRating)
                .statusBreakdown(statusBreakdown)
                .build();
    }

    @Override
    public List<CycleDetailDTO> getCycleDetails(String cycleName) {
        List<Appraisals> appraisals = getCycleOrThrow(cycleName);

        return appraisals.stream()
                .map(a -> CycleDetailDTO.builder()
                        .appraisalId(a.getAppraisalId())
                        .employeeEmail(a.getEmployee() != null
                                ? a.getEmployee().getFirstName() + " " + a.getEmployee().getLastName()
                                : "—")
                        .managerEmail(a.getManager() != null
                                ? a.getManager().getFirstName() + " " + a.getManager().getLastName()
                                : "—")
                        .deptName(a.getEmployee() != null && a.getEmployee().getDepartment() != null
                                ? a.getEmployee().getDepartment().getDeptName()
                                : "Unassigned")
                        .appraisalStatus(a.getAppraisalStatus())
                        .selfRating(a.getSelfRating() != null ? a.getSelfRating() : 0)
                        .managerRating(a.getManagerRating() != null ? a.getManagerRating() : 0)
                        .build())
                .collect(Collectors.toList());
    }
}