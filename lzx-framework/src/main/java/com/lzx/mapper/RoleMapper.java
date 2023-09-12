package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.domain.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-22 16:32:39
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> getRoleKeysByUserId(Long id);

    void saveRoleMenu(@Param("id") Long id, @Param("menuIds")List<Long> menuIds);

    void deleteOriginRoleMenu(Long id);
}

