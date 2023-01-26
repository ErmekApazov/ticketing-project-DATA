package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);
        return projectMapper.convertToDto(project);
    }

    @Override
    public List<ProjectDTO> listAllProjects() {

        List<Project> list = projectRepository.findAll(Sort.by("projectCode"));
        return list.stream().map(projectMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public void save(ProjectDTO dto) {

        dto.setProjectStatus(Status.OPEN); // done in MVC, check it!

        Project project = projectMapper.convertToEntity(dto); // get this dto, what id dto - convert to entity
        projectRepository.save(project);
        //or in one line
//        projectRepository.save(projectMapper.convertToEntity(dto));

    }

    @Override
    public void update(ProjectDTO dto) {
        //Find current project that has an id
        Project project = projectRepository.findByProjectCode(dto.getProjectCode()); // has id
        //Map update project dto to entity object
        Project convertedProject = projectMapper.convertToEntity(dto); // has id?
        //set id to the converted project
        convertedProject.setId(project.getId());
        //set status of the project
        convertedProject.setProjectStatus(project.getProjectStatus());
        //save the updated project in the db
        projectRepository.save(convertedProject);

    }

    @Override
    public void delete(String code) {
        // go to db and get that project with
        Project project = projectRepository.findByProjectCode(code);
        // change the isDeleted field to true
        project.setIsDeleted(true);

        //*cundullah
        // get the same projectCode that you deleted and add "-" :
        project.setProjectCode(project.getProjectCode() + "-" + project.getId()); // SP00-1



        // save the object in the DB
        projectRepository.save(project);

        taskService.deleteByProject(projectMapper.convertToDto(project)); // delete tasks related to the project



    }

    @Override
    public void complete(String projectCode) {
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

        taskService.completeByProject(projectMapper.convertToDto(project));
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {
        //get user-manager that I need to go db and ask db give me all projects assigned to/belongs to that certain manager
        UserDTO currentUserDTO = userService.findByUserName("harold@manager.com"); // right side after "=" is login manager

        //convert UserDTO to entity, go to db and check it
        User user = userMapper.convertToEntity(currentUserDTO); //User user - entity object

        // i need all projects belong to user-manager
        List<Project> list = projectRepository.findAllByAssignedManager(user);

        // return all project dto, and  dto + two fields included
        // .map() - take each project one by one (entity projects)
        return list.stream().map(
                project -> {
                        // convert each entity project to dto project
                   ProjectDTO obj = projectMapper.convertToDto(project);
                        //set two fields
//                   obj.setUnfinishedTaskCounts(3); // 3 is hard coded, this information should come from database
            // give project parameter to taskService, taskService will check for non-completed projects belong to certain project
                    obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()) );
                   obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
                   return obj;
                }
                //at the end returning list of project as a list<projectDto>
        ).collect(Collectors.toList());


    }

    @Override
    public List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager) {
        //1. all project from db that we get should be non completed projects
        //2. get project based on manager

        List<Project> projects = projectRepository
                .findAllByProjectStatusIsNotAndAssignedManager(Status.COMPLETE, userMapper.convertToEntity(assignedManager));
        //convert all entities to dto
        return projects.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }


}
