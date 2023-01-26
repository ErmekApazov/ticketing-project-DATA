package com.cydeo.repository;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findByProjectCode(String code); //returning entity object "Project"

    List<Project> findAllByAssignedManager(User manager); // (User manager) - go to db and give all projects assigned to that certain user-manager

    List<Project> findAllByProjectStatusIsNotAndAssignedManager(Status status, User assignedManager);


}
