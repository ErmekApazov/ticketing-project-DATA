package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;

import java.util.List;

public interface ProjectService {

    //what do we need in application?
    // update, delete, save, etc.

    ProjectDTO getByProjectCode(String code);
    List<ProjectDTO> listAllProjects();
    void save(ProjectDTO dto);
    void delete(String code);
    void update(ProjectDTO dto); // give me dto and update in db //    void delete(String code);
    void complete(String projectCode);
    List<ProjectDTO> listAllProjectDetails();

    List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager);
}
