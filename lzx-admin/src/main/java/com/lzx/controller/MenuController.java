package com.lzx.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.ListMenuDto;
import com.lzx.domain.entity.Menu;
import com.lzx.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult listMenu(ListMenuDto listMenuDto){
        return menuService.listMenu(listMenuDto);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable("id") Long id){
        return menuService.getMenuById(id);
    }
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteMenuById(@PathVariable("id") Long id){
        return menuService.deleteMenuById(id);
    }
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        return menuService.treeSelect();
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable("id") Long id){
        return menuService.roleMenuTreeselect(id);
    }
}
