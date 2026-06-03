package com.project.AppraisalSystem.repository;

import com.project.AppraisalSystem.entity.Department;
import com.project.AppraisalSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

}
