package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<RoleDTO> listAllRoles() { // return is dto
        //Controller call me and request all role dto so it can show in the drop-down in the ui
        // I need to make a call to db and get all the roles from table
        // business to database - go to repository and find a service(method) which gives me the roles from db

        // how I will call any service here?

//        roleRepository.findAll();//how it knows in which table to executed
        // is this method returning anything or just void? - List<Role>

        List<Role> roleList = roleRepository.findAll(); // roleList is entity

        //I have Role entities from DB
        // I need to convert those Role entities to DTOs
        //I need to use ModelMapper
        //I already created a class called RoleMapper and there are methods for me that will make this conversion

//        return roleList.stream().map(roleMapper::convertToDto).collect(Collectors.toList());
//        return roleList.stream().map(role->mapperUtil.convert(role, new RoleDTO())).collect(Collectors.toList()); 1 way of mapperUtil -convert method
        return roleList.stream()
                .map(role->mapperUtil.convert(role, RoleDTO.class))
                .collect(Collectors.toList()); // 2 way of conversion

//        return roleRepository.findAll().stream().map(roleMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public RoleDTO findById(Long id) {
        return roleMapper.convertToDto(roleRepository.findById(id).get());
        //
    }
}
