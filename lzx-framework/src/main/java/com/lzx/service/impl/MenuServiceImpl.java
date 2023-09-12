package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.ListMenuDto;
import com.lzx.domain.entity.Menu;
import com.lzx.domain.vo.*;
import com.lzx.enums.AppHttpCodeEnum;
import com.lzx.handler.exception.SystemException;
import com.lzx.mapper.MenuMapper;
import com.lzx.service.MenuService;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.SecurityUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-08-22 16:27:36
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> getPermissionsByUserId(Long id) {
        List<String> perms = null;

        //如果是管理员，menu中所有type为CF的permissions
        if (1 == id) {
            //设置wrapper
            LambdaQueryWrapper<Menu> lambdaQueryWrapper =
                    new LambdaQueryWrapper<>();
            lambdaQueryWrapper.in(Menu::getMenuType, "C", "F");
            lambdaQueryWrapper.eq(Menu::getStatus,
                    SystemConstants.MENU_STATUS_NORMAL);
            //根据wrapper查询menus
            List<Menu> menus = list(lambdaQueryWrapper);
            perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
        }
        else {
            //不是管理员
            perms = getBaseMapper().getPermissionsByUserId(id);
        }
        return perms;
    }

    @Override
    public ResponseResult getRouters() {
        //获取id
        Long id = SecurityUtils.getUserId();
        List<Menu> menus = new ArrayList<>();

        //如果是管理员1
        if(1 == id){
            menus = getBaseMapper().getAllRouters();
        }
        //不是管理员1
        else{
            menus = getBaseMapper().getRoutersByAdminId(id);
        }

        //构建树状结构
        menus = bulidTree(menus, 0L);
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @Override
    public ResponseResult listMenu(ListMenuDto listMenuDto) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(listMenuDto.getMenuName()),
                Menu::getMenuName, listMenuDto.getMenuName());
        wrapper.eq(StringUtils.hasText(listMenuDto.getStatus()),
                Menu::getStatus, listMenuDto.getStatus());
        wrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> menus = list(wrapper);

        List<MenuListVo> menuListVos =
                BeanCopyUtils.copyBeanList(menus, MenuListVo.class);

        return ResponseResult.okResult(menuListVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu menu = getById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        //如果父菜单是自己，报错
        if(menu.getParentId().equals(menu.getId())){
            throw new SystemException(AppHttpCodeEnum.MENU_PARENTID_CANNOT_EQUAL_SELFID);
        }

        LambdaUpdateWrapper<Menu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Menu::getId, menu.getId());
        update(menu, wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenuById(Long id) {
        List<Menu> menus = list();
        for (Menu menu: menus) {
            if(menu.getParentId().equals(id)){
                throw new SystemException(AppHttpCodeEnum.MENU_HAS_CHILD_MENU);
            }
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult treeSelect() {
        List<MenuTreeVo> menuTreeVosWithChildren = getMenuTreeVosWithChildren();
//        List<Menu> menuTree = bulidTree(menuOrigin, 0L);
//        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menuTree, MenuTreeVo.class);
        return ResponseResult.okResult(menuTreeVosWithChildren);
    }

    /**
     *
     * @return 获取所有菜单树
     */
    private List<MenuTreeVo> getMenuTreeVosWithChildren(){
        List<Menu> menuOrigin = list();
        List<MenuTreeVo> menuTreeVosWithoutChildren =
                BeanCopyUtils.copyBeanList(menuOrigin, MenuTreeVo.class);
        List<MenuTreeVo> menuTreeVosWithChildren = buildTree(menuTreeVosWithoutChildren, 0L);
        return menuTreeVosWithChildren;
    }

    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        //获取所有menu树
        List<MenuTreeVo> menuTreeVosWithChildren = getMenuTreeVosWithChildren();
        //获取角色关联的menu的id列表
        List<Long> menuIds = getBaseMapper().getMenuIdByRoleId(id);
        return ResponseResult.okResult(new RoleMenuTreeVo(menuTreeVosWithChildren, menuIds));
    }

    private List<MenuTreeVo> buildTree(List<MenuTreeVo> menuTreeVos, Long parentId){
        List<MenuTreeVo> menuTree = menuTreeVos.stream()
                .filter(menuTreeVo -> menuTreeVo.getParentId().equals(parentId))
                .map(menuTreeVo -> menuTreeVo.setChildren(getChildren(menuTreeVo, menuTreeVos)))
                .collect(Collectors.toList());
        return menuTree;
    }
    private List<MenuTreeVo> getChildren(MenuTreeVo menuTreeVo, List<MenuTreeVo> menuTreeVos) {
        if(Objects.isNull(menuTreeVo)){
            return null;
        }
        List<MenuTreeVo> menuTreeVoWithChildren = menuTreeVos.stream()
                .filter(menuTreeVo1 -> menuTreeVo1.getParentId().equals(menuTreeVo.getId()))
                .map(menuTreeVo1 -> menuTreeVo1.setChildren(getChildren(menuTreeVo1, menuTreeVos)))
                .collect(Collectors.toList());

        return menuTreeVoWithChildren;
    }
    private List<Menu> bulidTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        if(Objects.isNull(menu)){
            return null;
        }
        List<Menu> menuWithChildren = menus.stream()
                .filter(menu1 -> menu1.getParentId().equals(menu.getId()))
                .map(menu1 -> menu1.setChildren(getChildren(menu1, menus)))
                .collect(Collectors.toList());

        return menuWithChildren;
    }
}

