package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        // go to database, bring all users, convert entity to dto
        // 1. inject UserRepository inside UserServiceImpl
        // 2. findAll method through userRepository
        //3. mapper to convert entity to dto. go to Mapper package/UserMapper

        //findAll normally give all users (deleted, non-deleted), coz I removed @Where annot.
        //Therefor we should change findAll method to "findAllByIsDeletedOrderByFirstNameDesc" from UserRepository

//        List<User> userList = userRepository.findAll(Sort.by("firstName"));
        List<User> userList = userRepository.findAllByIsDeletedOrderByFirstNameDesc(false); // false - returning users that is_deleted is false / not deleted

        return userList.stream()
                .map(userMapper::convertToDto)
                .collect(Collectors.toList());


    }

    @Override
    public UserDTO findByUserName(String username) { // will be I seeing deleted users? yes // select * name where username == username
//        User user = userRepository.findByUserName(username);
        User user = userRepository.findByUserNameAndIsDeleted(username, false);
        return userMapper.convertToDto(user);
    }

    @Override
    public void save(UserDTO user) {
//      userRepository.save(user); // this save accepting entity, but you're giving DTO. cos it is coming from UI
        userRepository.save(userMapper.convertToEntity(user));
    }

//    this is hard deletion -
//    @Override
//    public void deleteByUserName(String username) {
//        userRepository.deleteByUserName(username);
//    }

    @Override
    public UserDTO update(UserDTO user) {
//
//        userRepository.save(userMapper.convertToEntity(user)); // save updated
//        return findByUserName(user.getUserName());
        // when you click save updated object you have an error and creating a new duplicated object.

        //Find current user
//        User user1 = userRepository.findByUserName(user.getUserName()); // has id
        User user1 = userRepository.findByUserNameAndIsDeleted(user.getUserName(), false); // has id

        //Map update user dto to entity object

//        user1.setUserName(user.getUserName()); ---> this line converts dto to entity.
//        but it works manually, applied to every field in the class. too much work

        User convertedUser = userMapper.convertToEntity(user); // has id?
        //set id to the converted object
        convertedUser.setId(user1.getId());
        //save the updated user in the db
        userRepository.save(convertedUser);

        return findByUserName(user.getUserName());


    }

    @Override
    public void delete(String username) {
        // go to db and get that user with username
//        User user = userRepository.findByUserName(username);
        User user = userRepository.findByUserNameAndIsDeleted(username, false);

        if (checkIfUserCanBeDeleted(user)){
            // change the isDeleted field to true
            user.setIsDeleted(true);

            user.setUserName(user.getUserName() + "-" + user.getId()); //harold@manager.com-1

            // save the object in the DB
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        //got to db
        //give me all user specific with

//      List<User> users = userRepository. //I don't have business to bring users with certain roles "admin", "manager etc.

//        List<User> users = userRepository.findByRoleDescriptionIgnoreCase(role);
        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role, false);

        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user){
        switch(user.getRole().getDescription()){
            case "Manager":
                List<ProjectDTO> projectDTOList =
                        projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
                        return projectDTOList.size() == 0;

            case "Employee":
                List<TaskDTO> taskDTOList =
                        taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));
                return taskDTOList.size() == 0;
            default:
                return true;
        }
    }
}
