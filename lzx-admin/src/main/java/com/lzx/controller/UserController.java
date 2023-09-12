package com.lzx.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddUserDto;
import com.lzx.domain.dto.ListUserDto;
import com.lzx.domain.dto.UpdateUserDto;
import com.lzx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult getUserList(Integer pageNum, Integer pageSize,
                                      ListUserDto listUserDto){
        return userService.getUserList(pageNum, pageSize, listUserDto);

    }
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteUserById(@PathVariable("id") Long id){
        return userService.deleteUserById(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }
    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return  userService.updateUser(updateUserDto);
    }
}
