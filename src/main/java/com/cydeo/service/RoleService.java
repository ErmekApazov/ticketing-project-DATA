package com.cydeo.service;

import com.cydeo.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    public List<RoleDTO> listAllRoles(); // why didn't we extend from JpaRepository and why RoleDTO?


    // where am i gonna call this method? - in controller

    RoleDTO findById(Long id);
}
