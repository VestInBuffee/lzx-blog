package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-22 16:27:35
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> getPermissionsByUserId(Long id);

    List<Menu> getAllRouters();

    List<Menu> getRoutersByAdminId(Long id);

    List<Long> getMenuIdByRoleId(Long id);
}

