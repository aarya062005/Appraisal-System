package com.project.AppraisalSystem.repository;

import com.project.AppraisalSystem.entity.Department;
import com.project.AppraisalSystem.entity.User;
import com.project.AppraisalSystem.entity.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);

    List<User> findAllByDepartment_DeptId(Long deptId);

    List<User> findAllByManager_UserId(Long managerId);
    List<User> findAllByRole(Roles role);
}
