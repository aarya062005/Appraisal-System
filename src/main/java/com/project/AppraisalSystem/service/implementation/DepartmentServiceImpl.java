package com.project.AppraisalSystem.service.implementation;

import com.project.AppraisalSystem.dto.DepartmentRequestDTO;
import com.project.AppraisalSystem.dto.DepartmentResponseDTO;
import com.project.AppraisalSystem.entity.Department;
import com.project.AppraisalSystem.exception.BadRequestException;
import com.project.AppraisalSystem.exception.DuplicateResourceException;
import com.project.AppraisalSystem.exception.ResourceNotFoundException;
import com.project.AppraisalSystem.repository.DepartmentRepository;
import com.project.AppraisalSystem.service.DepartmentService;

import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {


    private final DepartmentRepository departmentRepository;

    private final ModelMapper modelMapper;



    // GET ALL DEPARTMENTS
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponseDTO> findAll() {


        return departmentRepository.findAll()
                .stream()

                .sorted((a,b) ->
                        a.getDeptName()
                                .compareToIgnoreCase(b.getDeptName()))

                .map(this::mapToDTO)

                .collect(Collectors.toList());

    }





    // GET DEPARTMENT BY ID
    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO findById(Long deptId) {


        Department department =
                departmentRepository.findById(deptId)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found with id: "
                                                + deptId
                                ));


        return mapToDTO(department);

    }





    // GET DEPARTMENT BY NAME
    @Override
    @Transactional(readOnly = true)
    public DepartmentResponseDTO findByName(String deptName) {


        Department department =
                departmentRepository.findByDeptName(deptName)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found with name: "
                                                + deptName
                                ));


        return mapToDTO(department);

    }





    // CREATE DEPARTMENT
    @Override
    public DepartmentResponseDTO addDepartment(
            DepartmentRequestDTO dto) {


        if(dto.getDeptName() == null ||
                dto.getDeptName().isBlank()){

            throw new BadRequestException(
                    "Department name cannot be empty"
            );
        }



        departmentRepository.findByDeptName(
                        dto.getDeptName().trim()
                )

                .ifPresent(existing -> {

                    throw new DuplicateResourceException(
                            "Department already exists: "
                                    + dto.getDeptName()
                    );

                });



        Department department =
                modelMapper.map(dto, Department.class);



        department.setDeptName(
                department.getDeptName().trim()
        );



        Department saved =
                departmentRepository.save(department);



        return mapToDTO(saved);

    }





    // UPDATE FULL DEPARTMENT
    @Override
    public DepartmentResponseDTO updateDepartment(
            Long deptId,
            DepartmentRequestDTO dto) {


        Department existing =
                departmentRepository.findById(deptId)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found with id: "
                                                + deptId
                                ));



        departmentRepository.findByDeptName(
                        dto.getDeptName().trim()
                )

                .filter(found ->
                        !found.getDeptId().equals(deptId)
                )

                .ifPresent(found -> {

                    throw new DuplicateResourceException(
                            "Department name already taken: "
                                    + dto.getDeptName()
                    );

                });



        existing.setDeptName(
                dto.getDeptName().trim()
        );


        existing.setDeptDescription(
                dto.getDeptDescription()
        );



        Department updated =
                departmentRepository.save(existing);



        return mapToDTO(updated);

    }





    // DELETE DEPARTMENT
    @Override
    public boolean deleteDepartment(Long deptId) {


        Department existing =
                departmentRepository.findById(deptId)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found with id: "
                                                + deptId
                                ));



        departmentRepository.delete(existing);


        return true;

    }





    // PATCH DEPARTMENT
    @Override
    public DepartmentResponseDTO patchDepartment(
            Long deptId,
            DepartmentRequestDTO dto) {


        Department existing =
                departmentRepository.findById(deptId)

                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found with id: "
                                                + deptId
                                ));




        if(dto.getDeptName() != null){


            String newName =
                    dto.getDeptName().trim();



            if(newName.isBlank()){

                throw new BadRequestException(
                        "Department name cannot be blank"
                );

            }




            departmentRepository.findByDeptName(newName)

                    .filter(found ->
                            !found.getDeptId().equals(deptId)
                    )

                    .ifPresent(found -> {

                        throw new DuplicateResourceException(
                                "Department name already taken: "
                                        + newName
                        );

                    });



            existing.setDeptName(newName);

        }





        if(dto.getDeptDescription() != null){

            existing.setDeptDescription(
                    dto.getDeptDescription()
            );

        }




        Department updated =
                departmentRepository.save(existing);



        return mapToDTO(updated);

    }







    // COMMON MAPPER WITH EMPLOYEE COUNT
    private DepartmentResponseDTO mapToDTO(
            Department department){



        DepartmentResponseDTO dto =
                modelMapper.map(
                        department,
                        DepartmentResponseDTO.class
                );



        dto.setEmployeeCount(

                department.getUsers() == null

                        ? 0

                        : department.getUsers().size()

        );



        return dto;

    }

}