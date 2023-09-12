package com.lzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.ListMenuDto;
import com.lzx.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-08-22 16:27:36
 */
public interface MenuService extends IService<Menu> {

    List<String> getPermissionsByUserId(Long id);

    ResponseResult getRouters();

    ResponseResult listMenu(ListMenuDto listMenuDto);

    ResponseResult addMenu(Menu menu);

    ResponseResult getMenuById(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenuById(Long id);

    ResponseResult treeSelect();

    ResponseResult roleMenuTreeselect(Long id);
}

