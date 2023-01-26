package com.cydeo.repository;

import com.cydeo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> { // I am building DAO pattern

    //give me the role based on the description
    //derived query

    Role findByDescription(String description);




}
