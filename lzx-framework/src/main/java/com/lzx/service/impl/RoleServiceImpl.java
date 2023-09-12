package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddRoleDto;
import com.lzx.domain.dto.ChangeStatusRoleDto;
import com.lzx.domain.dto.ListRoleDto;
import com.lzx.domain.dto.UpdateRoleDto;
import com.lzx.domain.entity.Role;
import com.lzx.domain.vo.PageVo;
import com.lzx.domain.vo.RoleUpdateVo;
import com.lzx.domain.vo.RoleVo;
import com.lzx.mapper.RoleMapper;
import com.lzx.service.RoleService;
import com.lzx.utils.BeanCopyUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-08-22 16:32:40
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> getRoleKeysByUserId(Long id) {
        List<String> roleKeys = new ArrayList<>();

        //如果是管理员，直接返回admin
        if(1 == id){
            roleKeys.add("admin");
        }
        //不是管理员
        else {
            roleKeys = getBaseMapper().getRoleKeysByUserId(id);
        }
        return roleKeys;
    }

    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, ListRoleDto listRoleDto) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(listRoleDto.getRoleName()),
                Role::getRoleName, listRoleDto.getRoleName());
        wrapper.eq(StringUtils.hasText(listRoleDto.getStatus()),
                Role::getStatus, listRoleDto.getStatus());
        wrapper.orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVos, page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(ChangeStatusRoleDto dto) {
        Role role = BeanCopyUtils.copyBean(dto, Role.class);
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        //保存到role表
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        //保存到role_menu表
        getBaseMapper().saveRoleMenu(role.getId(), addRoleDto.getMenuIds());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleById(Long id) {
        Role role = getById(id);
        RoleUpdateVo roleUpdateVo = BeanCopyUtils.copyBean(role, RoleUpdateVo.class);
        return ResponseResult.okResult(roleUpdateVo);
    }

    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        //更新role表
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        updateById(role);
        //更新role_menu数据库
        getBaseMapper().deleteOriginRoleMenu(role.getId());
        getBaseMapper().saveRoleMenu(role.getId(), updateRoleDto.getMenuIds());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRoleById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        List<Role> roles = getAllRole();
        return ResponseResult.okResult(roles);
    }
    public List<Role> getAllRole(){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, SystemConstants.ROLE_STATUS_NORMAL);
        List<Role> roles = list(wrapper);
        return roles;
    }
}

