package com.project.AppraisalSystem.service.implementation;

import com.project.AppraisalSystem.dto.CycleAppraisalDetailDTO;
import com.project.AppraisalSystem.dto.CycleReportDTO;
import com.project.AppraisalSystem.entity.Appraisals;
import com.project.AppraisalSystem.entity.enums.AppraisalStatus;
import com.project.AppraisalSystem.repository.AppraisalsRepository;
import com.project.AppraisalSystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final AppraisalsRepository appraisalsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCycles() {
        return appraisalsRepository.findAll()
                .stream()
                .map(Appraisals::getCycleName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CycleReportDTO getCycleReport(String cycleName) {
        List<Appraisals> appraisals = appraisalsRepository.findAll()
                .stream()
                .filter(a -> a.getCycleName().equals(cycleName))
                .collect(Collectors.toList());

        int total = appraisals.size();
        int acknowledged = (int) appraisals.stream()
                .filter(a -> a.getAppraisalStatus() == AppraisalStatus.ACKNOWLEDGED)
                .count();
        int completion = total > 0 ? (acknowledged * 100) / total : 0;
        int pendingAction = total - acknowledged;

        Double avgSelfRating = appraisals.stream()
                .filter(a -> a.getSelfRating() != null && a.getSelfRating() > 0)
                .mapToInt(Appraisals::getSelfRating)
                .average()
                .orElse(0.0);

        Double avgManagerRating = appraisals.stream()
                .filter(a -> a.getManagerRating() != null && a.getManagerRating() > 0)
                .mapToInt(Appraisals::getManagerRating)
                .average()
                .orElse(0.0);

        Map<String, Long> statusBreakdown = appraisals.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getAppraisalStatus().name(),
                        Collectors.counting()
                ));

        return CycleReportDTO.builder()
                .cycleName(cycleName)
                .totalAppraisals(total)
                .acknowledged(acknowledged)
                .completion(completion)
                .pendingAction(pendingAction)
                .avgSelfRating(avgSelfRating)
                .avgManagerRating(avgManagerRating)
                .statusBreakdown(statusBreakdown)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CycleAppraisalDetailDTO> getCycleAppraisalDetails(String cycleName) {
        return appraisalsRepository.findAll()
                .stream()
                .filter(a -> a.getCycleName().equals(cycleName))
                .map(a -> CycleAppraisalDetailDTO.builder()
                        .appraisalId(a.getAppraisalId())
                        .employeeEmail(a.getEmployee().getEmail())
                        .managerEmail(a.getManager().getEmail())
                        .deptName(a.getEmployee().getDepartment() != null
                                ? a.getEmployee().getDepartment().getDeptName() : "—")
                        .appraisalStatus(a.getAppraisalStatus())
                        .selfRating(a.getSelfRating())
                        .managerRating(a.getManagerRating())
                        .build())
                .collect(Collectors.toList());
    }
}
