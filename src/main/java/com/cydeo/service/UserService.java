package com.cydeo.service;

import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserService {

    List<UserDTO> listAllUsers(); // in controller will be called
    UserDTO findByUserName(String username); // for update in user interface based on username
    void save(UserDTO user);
//    void deleteByUserName(String username);
    UserDTO update(UserDTO user);
    void delete(String username);

    //ProjectController calls this method
    List<UserDTO> listAllByRole(String role);


}
