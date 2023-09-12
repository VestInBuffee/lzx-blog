package com.lzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddRoleDto;
import com.lzx.domain.dto.ChangeStatusRoleDto;
import com.lzx.domain.dto.ListRoleDto;
import com.lzx.domain.dto.UpdateRoleDto;
import com.lzx.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-08-22 16:32:40
 */
public interface RoleService extends IService<Role> {

    List<String> getRoleKeysByUserId(Long id);

    ResponseResult getRoleList(Integer pageNum, Integer pageSize, ListRoleDto listRoleDto);

    ResponseResult changeStatus(ChangeStatusRoleDto dto);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRoleById(Long id);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult deleteRoleById(Long id);

    ResponseResult listAllRole();

    List<Role> getAllRole();
}

