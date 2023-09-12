package com.lzx.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddRoleDto;
import com.lzx.domain.dto.ChangeStatusRoleDto;
import com.lzx.domain.dto.ListRoleDto;
import com.lzx.domain.dto.UpdateRoleDto;

import com.lzx.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/list")
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize,
                                      ListRoleDto listRoleDto){
        return roleService.getRoleList(pageNum, pageSize, listRoleDto);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeStatusRoleDto dto){
        return roleService.changeStatus(dto);
    }
    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable Long id){
        return roleService.getRoleById(id);
    }
    @PutMapping
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteRoleById(@PathVariable("id") Long id){
        return roleService.deleteRoleById(id);
    }
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
